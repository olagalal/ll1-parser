# LL(1) Parser

[![Shields.io](https://img.shields.io/badge/type-college%20project-orange?style=flat)](http://shields.io/)

LL(1) parser (Left-to-right, Leftmost derivation) is a top-down parser for a subset of context-free languages. It parses the input from Left to right, performing Leftmost derivation of the sentence. This project is LL(1) parser implemented using java. It takes the set of rules as text and then generates the output. This project was made for **(Compiler - Forth Year)** in my college.

### Input Example
```
4 S S ABc A bA A phi B c
```
![Alt text](https://drive.google.com/uc?id=1QIb9YazK1QKrOVbN9X9WnhEPC9CnNZc5 "Screenshot")

### Output Screenshot
![Alt text](https://drive.google.com/uc?id=11vcgDup5GOW-Y2mc0NBW69AUNjWWQ90U "Screenshot")
![Alt text](https://drive.google.com/uc?id=1nun4ob3F4L9l8qH8ndxyKizU53cO4NPB "Screenshot")
![Alt text](https://drive.google.com/uc?id=1G7PN7cxE5u9XFW1zGk91PgJufJ4i_iwd "Screenshot")

### other examples you could try
```
7 S S ABD A aA A phi B bB B phi D dD D phi

8 E E TO O pTO O phi T FR R aFR R phi F bEc F v
```

## License
This project is available under the terms of [MIT License](https://choosealicense.com/licenses/mit/)
