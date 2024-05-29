# Sparse Matrix Operations
## Overview
This project implements operations for sparse matrices in Java without using built-in libraries like java.util. The operations supported are addition, subtraction, and multiplication. Sparse matrices are read from input files, and the results are written to output files.

### Input File Format


rows=8433

cols=3180

(0, 381, -694)

(0, 128, -838)

(0, 639, 857)

(0, 165, -933)

(0, 1350, -89)

### Entry Format
(0, 1350, -89): `0` stands for the row, `1350` stands for the column and `-89` is the value.

### Output File Format
The output file will have the same format as the input file, containing the resulting sparse matrix after the selected operation.



You will be prompted to choose an operation (addition, subtraction, multiplication).
The result will be written to the appropriate file in the output directory.

### Error Handling
The program will throw an IllegalArgumentException if the input file format is incorrect.
Matrix operations will throw an IllegalArgumentException if the matrix dimensions do not match the requirements for the operation.
