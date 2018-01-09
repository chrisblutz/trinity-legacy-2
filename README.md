[![Trinity Logo](https://raw.githubusercontent.com/trinity-lang/trinity/master/logo/logo-horizontal.png)](https://github.com/trinity-lang/trinity)

# [![Build Status](https://travis-ci.org/trinity-lang/trinity.svg?branch=master)](https://travis-ci.org/trinity-lang/trinity) [![Build status](https://ci.appveyor.com/api/projects/status/6yf5vlo74j07sbqx/branch/master?svg=true)](https://ci.appveyor.com/project/trinity-lang/trinity/branch/master)

Trinity is a dynamically-typed, object-oriented programming language.
It is heavily influenced by aspects of [Java](https://www.java.com/) and [Ruby](https://www.ruby-lang.org/).

#### Repository Structure
- `bin` - Batch/Shell scripts
- `lib` - Trinity standard library
- `logo` - PNG/SVG logos for Trinity
- `src` - Java source code for the Trinity interpreter
- `test` - Trinity utility code for use with the interpreter unit tests

## Features
- Basic object-oriented features like classes, methods, inheritance, etc.
- Method monkey-patching (overriding previously-defined methods)
- Inline procedures (i.e. `X.y { |z| ... }`)
- Global variables (like `$ARGV` for command-line arguments)
- Weak, dynamic typing system
- Automatic numeric type conversion/overflow (see [Numeric Automation](https://github.com/trinity-lang/trinity/wiki/Quirks-and-Features#numeric-automation))
- An interactive shell (`trinity -ish`)

## Getting Started
Before you start using Trinity you need to download the Trinity interpreter.  This can be done by either downloading the most recent release or by downloading the source code.  For the purposes of this tutorial, we will be building the interpreter from the source code.  Once you have the source code downloaded and extracted, open the terminal inside the directory you just extracted the source into and type:
```sh
./gradlew build
```
This should build the `trinity.jar` inside of a `bin/` folder.  To make life easier down the road, consider adding that `bin/` folder to your `PATH` environment variable.

The fastest way to begin using Trinity is through its interactive shell.  From the terminal you can type the following to launch the interactive shell (assuming you added the `bin` directory to your `PATH`):
```sh
trinity -ish
```

Another way to use Trinity is through its source files, which carry the extension `.ty`.  Files do not have any enforced naming requirements, so it does not matter what you call the file.  To use it, pass the file path and any command-line arguments to the `trinity` command:
```sh
trinity HelloWorld.ty arg1 arg2 ...
```

## Development
Follow Trinity's development plans on [Trello](https://trello.com/b/dhVOwmwp).

## Copyright
Copyright &copy;2017 Christopher Lutz.  See [LICENSE](LICENSE) for further details.
