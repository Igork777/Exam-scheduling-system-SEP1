## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Implemented functionality](#implemented-functionality)

## General info
This project is an exam scheduling system for the secretary from VIA University College.
The back-end is written in Java.
The GUI is written in JavaFX.
## Technologies
Project is created with:
* Java
* JavaFX


## Setup
To run this project

1) Download Intelij Idea
2) Install SDK 11 or higher
3) Install JavaFX for your operational system (the one which is provided in the project is for Windows machines)
4) Go to Settings > Build, Execution, Deployment > Compiler > Java Compiler and make sure target bytecode is the same for sep1 and Exam-scheduling-system projects, and it is equal or higher than the 11th version.
5) Go to file > Project structure > Project Settings > Modules > Sources  and make sure that module SDK is the same as the Java Compiler version
6) Go to file > Project structure > Project Settings > Modules > Dependencies and make sure that module SDK is the same as the Java Compiler version
7) From  Project structure > Project Settings > Modules > Dependencies, make sure that JavaFX exist and are chosen. If not, choose them and press the button, apply
8) If you cannot run the client, which is MainGUI.java got to file > Project structure > Platform Settings > Global Libraries, then press + sign, choose Java, then find your JavaFX folder and select the lib folder. Then click apply.
9) You are ready to use our fantastic app =)

## Implemented functionality

* The secretary can create a template
for the exam term and then add
exams to it
* The secretary can edit all the
necessary parameters
* The secretary is alerted if she makes
a mistake in the scheduling.
* The system saves all the
information into a file, so the
secretary does not have to do the
whole schedule from scratch every
exam term.
* The secretary is able to export an
XML file to be handled by the
website, so the students and
teachers can see the exam schedule
* The secretary can specify the date
interval for the exam term because
it varies
* Secretary can only assign oral and
semester exams to classrooms with
a projector
* At least the first four semesters of
Software Engineering at Campus
Horsens are involved in the exam
scheduling
* The teachers have one hour lunch
break
* The secretary is able to specify the
duration of exams
* The secretary should assign a
student his respective home
classroom if the exam type is oral
* The secretary can modify the
availability of teachers in the
system
* A secretary should be given a
warning if she tries to give student
exams on consecutive days



