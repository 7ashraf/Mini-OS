
# Mini OS Project

This project aims to provide a simulation of an operating system by implementing a basic interpreter, memory management, mutexes for mutual exclusion, and a scheduler for process scheduling. The main objective is to understand the concepts of an operating system by building and experimenting with it.

## Project Overview

The project revolves around creating an interpreter that can read and execute programs stored in text files. Each text file represents a program, and when the interpreter reads and starts executing the code, it becomes a process. The project provides three program files as examples.

## Features

The following features are expected to be implemented in this project:

1. **Interpreter:** Implement an interpreter that can read program files (txt files) and execute their code. The interpreter should be able to handle the syntax and semantics of the programming language used in the program files.

2. **Memory Management:** Create a memory system to store the processes and their associated data. The memory should provide efficient allocation and deallocation of resources for processes.

3. **Mutexes:** Implement mutexes to ensure mutual exclusion over critical resources. Mutexes help in synchronizing access to shared resources and prevent data inconsistencies when multiple processes are running concurrently.

4. **Scheduler:** Develop a scheduler that schedules the processes in the system. The scheduler should determine the order in which processes are executed, considering factors like priority, waiting time, and fairness.
