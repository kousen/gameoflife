# SonarCloud Integration Guide

This document describes how to integrate SonarCloud for continuous code quality and security analysis.

## Prerequisites

1. **SonarCloud Account**: Sign up at [sonarcloud.io](https://sonarcloud.io)
2. **GitHub Repository**: Your project must be in a GitHub repository
3. **Organization**: Create or join a SonarCloud organization linked to your GitHub account

## Setup Steps

### 1. Import Project to SonarCloud

1. Log in to [SonarCloud](https://sonarcloud.io)
2. Click "+" → "Analyze new project"
3. Select your GitHub repository
4. Choose "Set up with GitHub Actions" as the analysis method

### 2. Configure Repository Secrets

Add these secrets to your GitHub repository (Settings → Secrets and variables → Actions):

- `SONAR_TOKEN`: Generated from SonarCloud (My Account → Security → Generate Token)
- `SONAR_HOST_URL`: Usually `https://sonarcloud.io`

### 3. Create SonarCloud Properties File

Create `sonar-project.properties` in your project root:

```properties
# Project identification
sonar.projectKey=your-github-username_gameoflife
sonar.organization=your-sonarcloud-org

# Project metadata
sonar.projectName=Game of Life
sonar.projectVersion=1.0.0

# Source and test directories
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=build/classes/java/main
sonar.java.test.binaries=build/classes/java/test

# Coverage reporting
sonar.coverage.jacoco.xmlReportPaths=build/reports/jacoco/test/jacocoTestReport.xml

# Java version
sonar.java.source=25
sonar.java.target=25

# Exclusions (optional)
sonar.exclusions=**/Main.java
sonar.test.exclusions=**/*Test.java

# Quality Gate
sonar.qualitygate.wait=true
```

### 4. Update Gradle Build

Add the SonarQube plugin to `build.gradle.kts`:

```kotlin
plugins {
    java
    application
    jacoco
    id("org.sonarqube") version "6.0.1.5171"
}

// ... existing configuration ...

sonar {
    properties {
        property("sonar.projectKey", "your-github-username_gameoflife")
        property("sonar.organization", "your-sonarcloud-org")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

// Ensure JaCoCo XML report is generated for SonarCloud
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)  // Required for SonarCloud
        html.required.set(true)
        csv.required.set(false)
    }
}
```

### 5. Create GitHub Actions Workflow

Create `.github/workflows/sonarcloud.yml`:

```yaml
name: SonarCloud Analysis

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

permissions:
  contents: read
  pull-requests: read

jobs:
  sonarcloud:
    name: SonarCloud Scan
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4
      with:
        fetch-depth: 0  # Shallow clones disabled for better analysis

    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'  # SonarCloud may not support Java 25 yet
        distribution: 'temurin'

    - name: Setup Gradle
      uses: gradle/actions/setup-gradle@v3

    - name: Cache SonarCloud packages
      uses: actions/cache@v4
      with:
        path: ~/.sonar/cache
        key: ${{ runner.os }}-sonar
        restore-keys: ${{ runner.os }}-sonar

    - name: Build and analyze
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
      run: |
        ./gradlew build jacocoTestReport sonar --info
```

### 6. Alternative: Integrate into Existing CI

Add SonarCloud analysis to your existing CI workflow:

```yaml
    - name: SonarCloud Scan
      if: github.event_name != 'pull_request' || github.event.pull_request.head.repo.full_name == github.repository
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
      run: ./gradlew sonar --info
```

## Quality Gates

SonarCloud default quality gates check:
- Coverage: ≥80% on new code
- Duplicated Lines: <3% on new code
- Maintainability Rating: A
- Reliability Rating: A
- Security Rating: A
- Security Hotspots Reviewed: 100%

You can customize these in SonarCloud project settings.

## Local Analysis

Run SonarCloud analysis locally:

```bash
# Set environment variables
export SONAR_TOKEN="your-token-here"
export SONAR_HOST_URL="https://sonarcloud.io"

# Run analysis
./gradlew build jacocoTestReport sonar
```

## Badge Integration

Add quality badges to your README:

```markdown
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=your-key&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=your-key)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=your-key&metric=coverage)](https://sonarcloud.io/summary/new_code?id=your-key)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=your-key&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=your-key)
```

## Troubleshooting

### Common Issues

1. **Java Version Mismatch**: SonarCloud might not support Java 25 yet. Use Java 21 for analysis.

2. **Missing Coverage**: Ensure JaCoCo XML reports are generated:
   ```bash
   ./gradlew clean test jacocoTestReport
   ```

3. **Authentication Failed**: Verify your SONAR_TOKEN is valid and has proper permissions.

4. **Analysis Timeout**: Large projects may timeout. Consider excluding generated code or test files.

### Useful Commands

```bash
# Check current coverage
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html

# Run SonarCloud analysis with debug info
./gradlew sonar --debug

# Clean and rebuild before analysis
./gradlew clean build jacocoTestReport sonar
```

## Resources

- [SonarCloud Documentation](https://docs.sonarsource.com/sonarcloud/)
- [SonarQube Gradle Plugin](https://docs.sonarsource.com/sonarqube/latest/analyzing-source-code/scanners/sonarscanner-for-gradle/)
- [JaCoCo Integration](https://docs.sonarsource.com/sonarcloud/enriching/test-coverage/java-test-coverage/)
- [GitHub Actions Integration](https://docs.sonarsource.com/sonarcloud/advanced-setup/ci-based-analysis/github-actions-for-sonarcloud/)

## Notes

- SonarCloud is free for open-source projects
- Private projects require a paid plan
- Analysis results are visible at: `https://sonarcloud.io/project/overview?id=your-project-key`
- Pull request decoration requires proper GitHub permissions