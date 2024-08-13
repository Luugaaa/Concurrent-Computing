# Concurrent Programming in Java

## Overview

This repository contains my exploration of concurrent programming in Java, demonstrated through three practical applications: **Pi Calculation**, **Parallel Merge Sort**, and **Game of Life**. These projects were developed as part of a deeper study into concurrent programming techniques, showcasing both the power and the complexity of threading in Java.

## Table of Contents

1. [Introduction](#introduction)
2. [Pi Calculation](#pi-calculation)
    - [Code Structure](#code-structure-pi)
    - [Concurrency](#concurrency-pi)
3. [Parallel Merge Sort](#parallel-merge-sort)
    - [Code Structure](#code-structure-merge-sort)
    - [Concurrency](#concurrency-merge-sort)
4. [Game of Life](#game-of-life)
    - [Code Structure](#code-structure-game-of-life)
    - [Concurrency](#concurrency-game-of-life)
    - [Next Generation Calculation](#next-generation-calculation)
    - [Graphical Interface Update](#graphical-interface-update)
5. [Conclusion](#conclusion)

## Introduction

Concurrent programming in Java allows developers to execute multiple tasks simultaneously, improving performance by dividing complex tasks into smaller, parallel processes. This repository illustrates the application of concurrency through three projects: calculating Pi, implementing parallel merge sort, and simulating the Game of Life.

## Pi Calculation

In this project, the value of Pi is estimated using the Monte Carlo method, parallelized using Java threads.

### Code Structure

- **Variables and Initialization**: A `resultats` array stores local probabilities computed by each thread. Each thread, an instance of the `MyThread` inner class, generates random points and updates its local probability.
  
### Concurrency

- **Thread Creation**: Four threads are instantiated from the `MyThread` class, each generating points within a unit square and updating the local probability.
- **Thread Synchronization**: Access to the `resultats` array is synchronized using `synchronized (this)` blocks to prevent concurrent access issues.
- **Global Probability Calculation**: After all threads finish, the global probability is computed by summing the local probabilities, which is then used to estimate Pi.

## Parallel Merge Sort

This project implements a parallel version of the classic merge sort algorithm, using threads to improve sorting efficiency.

### Code Structure

- **Main Method (`triFusion`)**: The list is divided into two parts (left and right), and parallel merge sort is applied using two threads.
- **Thread Creation**: Two threads are created to sort the left and right parts of the list.
- **Merge Method (`fusionner`)**: Once sorted, the left and right parts are merged into a single sorted list.

### Concurrency

- **Thread Utilization**: Two threads (`threadGauche` and `threadDroite`) parallelize the sorting of the left and right list parts.
- **Thread Synchronization**: The `join` method ensures that merging only occurs after both parts are sorted.

## Game of Life

This project simulates the "Game of Life" using concurrent programming to calculate the next generation of cells in parallel.

### Code Structure

- **Variables and Initialization**: The grid is represented by `grid` and `nextGenGrid`, while `random` is used for random initialization. The graphical interface is managed by `CellPanel`.
- **Graphical Interface**: `CellPanel` extends `JPanel` to display the grid, with each cell shown as a colored rectangle.

### Concurrency

- **Thread Creation**: When the "Next" button is pressed, four threads are created to calculate the next generation of the grid.
- **Thread Synchronization**: Modifications to `nextGenGrid` are synchronized to avoid concurrent access issues.
- **CountDownLatch**: A `CountDownLatch` object synchronizes the completion of all threads before updating the main grid.

### Next Generation Calculation

- **Game Rules**: The `calculateNextGenState` method applies the Game of Life rules to determine each cell's state in the next generation.
- **Grid Initialization**: The grid is initialized with random live and dead cells using the `initializeGrid` method.
- **Neighbor Counting**: The `countLiveNeighbors` method counts the live neighbors of a cell.

### Graphical Interface Update

- **Panel Redrawing**: The graphical interface is updated every generation to reflect grid changes, with live cells shown in white and dead cells in black.
- **Main Grid Update**: Once all threads have completed, the main grid is updated with the next generation.

## Conclusion

Through these three projects, this repository explores the benefits and challenges of concurrent programming in Java. The Pi calculation leveraged threading to distribute computation, the parallel merge sort showcased threading for sorting large datasets, and the Game of Life demonstrated concurrency in a complex simulation. These examples underline the importance of proper thread management and synchronization to fully harness the power of concurrent programming.
