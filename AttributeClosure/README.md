# Attribute Closure

## Usage
```
$ java ClosureOfAttributes <dependency text file> <closure set you want>
```
- Dependency text file will have the first line with all the attributes, then the FDs are the lines with a space between the left-hand side and right-hand side
- Closure set is whatever closure set you are looking for

## Example
- Example: Given the relation R(ABCDE) with FDs {A->B, CD->E, AC->D}
- dependencyfile.txt will have

```
			ABCDE
			A B
			CD E
			AC D
```

- Find the closure of (AC)+

```
$ java ClosureOfAttributes dependencyfile.txt AC
$ (AC)+ = {ABCDE}
```
