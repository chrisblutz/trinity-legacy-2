[![Trinity Logo](https://raw.githubusercontent.com/trinity-lang/trinity/master/logo/logo-horizontal.png)](https://github.com/trinity-lang/trinity)

Trinity is a dynamically-typed, object-oriented programming language.
It is heavily influenced by aspects of [Java](https://www.java.com/) and [Ruby](https://www.ruby-lang.org/).

#### Repository Structure
- `bin` - Batch/Shell scripts
- `lib` - Trinity standard library
- `logo` - PNG/SVG logos for Trinity
- `src` - Java source code for the Trinity interpreter

## Features
- Basic object-oriented features like classes, methods, inheritance, etc.
- Method monkey-patching (overriding previously-defined methods)
- Inline procedures (i.e. `X.y { |z| ... }`)
- Global variables (like `$ARGV` for command-line arguments)
- Flexible typing system (see [Typing System](#typing-system))
- Automatic numeric type conversion/overflow (see [Numeric Automation](#numeric-automation))
- An interactive shell (`trinity -ish`)

### Typing System
Trinity's weak, dynamic typing system allows it to be very flexible.  By design, the standard library contains methods to support both traditional runtime type checking (see [Traditional](#traditional)) and less common type-checking methods such as duck-typing (see [Duck-Typing](#duck-typing)).

#### Traditional
Trinity provides features that support the traditional runtime type checker, where types of objects are checked to determine if they will behave correctly.  These features include interfaces, which when implemented by classes, enforce the existence of certain methods.  On the other hand, methods such as `Object.instanceOf(...)` provide the framework for checking object types before use.

#### Duck-Typing
Trinity also provides features that support duck-typing, which stems from the phrase: *If it walks like a duck and it quacks like a duck, then it must be a duck*.  Trinity's `Object` class contains a `respondsTo(...)` method to determine if an object implements a certain method and therefore will behave as expected.

### Numeric Automation
Numeric types in Trinity automatically convert between each other.  If an `Int` overflows, it automatically becomes a `Long`.  If a `Long` underflows, it automatically becomes an `Int`.  If either an `Int` or a `Long` undergoes computation and becomes a floating-point value, it automatically becomes a `Float`.  If a `Float` loses its fractional component, it automatically becomes an `Int` or a `Long`.  This conversion feature allows Trinity to perform mathematical operations seamlessly, without manual conversions between types that might be found in statically-typed languages such as Java.

For example, this Java code:
```java
int a = 10;
double x = (double) a / 4; // => 2.5
```
looks like this in Trinity:
```
a = 10
x = a / 4                   # => 2.5
```

Trinity's automatic conversion also reduces confusion that might be found in the manual conversions.  For example, this Java code *does not* give the same output as this Trinity code:
```java
double x = 10 / 4 // => 2
```
```
x = 10 / 4         # => 2.5
```

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
