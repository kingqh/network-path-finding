#!/bin/bash

# 发布版本准备

mvn release:prepare -Darguments="-DskipTests"  -DautoVersionSubmodules=true -DscmCommentPrefix="chore: [maven-release-plugin] "  

#  发布版本执行

mvn release:perform -Darguments="-DskipTests" 
