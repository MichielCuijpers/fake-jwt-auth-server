# Fake JWT Auth Server

[![Build](https://github.com/michaelruocco/fake-jwt-auth-server/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/fake-jwt-auth-server/actions)
[![codecov](https://codecov.io/gh/michaelruocco/fake-jwt-auth-server/branch/master/graph/badge.svg?token=FWDNP534O7)](https://codecov.io/gh/michaelruocco/fake-jwt-auth-server)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/272889cf707b4dcb90bf451392530794)](https://www.codacy.com/gh/michaelruocco/fake-jwt-auth-server/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/fake-jwt-auth-server&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/michaelruocco/fake-jwt-auth-server?branch=master)](https://bettercodehub.com/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_fake-jwt-auth-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_fake-jwt-auth-server)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_fake-jwt-auth-server&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_fake-jwt-auth-server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_fake-jwt-auth-server&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_fake-jwt-auth-server)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_fake-jwt-auth-server&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_fake-jwt-auth-server)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/fake-jwt-auth-server.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22fake-jwt-auth-server%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview

This library creates a web server that you can use to fake a real JWT token auth server.

If you are not familiar with JSON Web Key (JWK) or JSON Web Token (JWT) there is a
good overview and description [here](https://www.baeldung.com/spring-security-oauth2-jws-jwk).

The fake web server provides two endpoints that are described below:

1. ```.well-known/jwks.json``` that returns a JWK set containing a single key definition
2. ```/oauth/token``` that returns a JWT signed with the key returned from the above endpoint

If you are building an application that uses JWTs for accessing secured endpoints, you can 
configure your application to point at the JWKs endpoint, so that it can use the JWK to decode
the JWT bearer token that you provide in your request. Then when you want to call your secured
endpoint you can call the JWT endpoint to generate the token to use in your Authorization header.

## Useful Commands

```gradle
// cleans build directories
// prints currentVersion
// formats code
// builds code
// runs tests
// checks for gradle issues
// checks dependency versions
./gradlew clean currentVersion dependencyUpdates lintGradle spotlessApply build
```