# Extension Methods in Java

In programming languages like Java a class is defined in a single Java file,
together with all its methods. Smalltalk and other languages support the concept
of "Extension Methods". This allows separating method definitions from class 
definitions. In some languages it is even possible to "extend" a runtime/system 
class (like "String") with new methods, exclusively for your application. 

Extensions methods are useful, e.g. if you cannot modify the original class.
Also, extension methods allow for better separation of concerns, more readable 
code etc. For more details on Extension methods see 
https://en.wikipedia.org/wiki/Extension_method.

This project demonstrates how to emulate extension methods in Java.

## The Approach

### Static Methods as Extension Methods

The basic idea behind our approach to use extension methods in Java is to 
define the extra methods as static methods in a separate "Extension class".

#### Defining Extension Methods

Instead of adding the method

    void m(...) {
        ...
    }

to the class `C` you create a new class `C_ext` and add the method statically:

    final class C_ext {
        private C_ext() {}

        void m(C self, ...) {
            ...
        }
    }

Notice the extra parameter `C self`. This parameter will hold the "this" value.
In your body of `m` you need to replace every usage of `this` 
(also the implicit ones) with `self`.

#### Calling Extension Methods

Instead of calling an object's method using the standard 'dot' notation:

    C c = ...
    ...
    c.m(...);
    
you instead call the static extension method, passing the object/receiver as 
its `self` parameter:

    C c = ...
    ...
    C_ext.m(c, ...);

#### Is that all?

The idea of implementing extra functionality for a class in separate static 
methods is very popular. Actually this is the way a lot of "Utility" classes 
are designed: they provide a set of static methods implementing some "commonly
used" features, using the arguments passed into the methods.

The tricky part with extension methods starts when you want to cover not only
simple method calls, but also enable extension methods to be included in the
type hierarchy. I.e. you want some kind of "method overriding" also for 
extension methods, doing "super calls", or even allow a method to be defined 
either as a normal method or an extension method, in the same type hierarchy.

The main part of the following will give the details how to do all this.

### Method calling 'super' to Extension method

Let's first recap what a "super" call actually means. Assume we have this code:

    class C {
        void m() {
            System.out.println("C.m()");
        }
    }

    class D extends C {
        void m() {
            super.m();
            System.out.println("D.m()");
        }
    }

The `super.m()` call in `D` says: 
"execute the method `m()` of my super class, using my `this`." 
_(If the direct super class does not implement `m()` go up the hierarchy until
you find an implementation for `m()`.)_ 

At compile time we know the whole super class chain from the method containing 
`super.m()`  and we know all implementations of method `m()` in that chain. So 
we actually know the exact method implementation to be executed with the
`super.m()` call, no polymorphism involved here.

Translated to our "Extension methods" scenario we can now easily implement a 
`super` call to an extension methods:
just call the static method of the super class' extension class:


    class C_ext {
        static void m(C self) {
            System.out.println("C.m()");
        }
    }

    class D extends C {
        void m() {
            C_ext.m(self);
            System.out.println("D.m()");
        }
    }

Notice this approach is the same, if you are calling `super` from a "normal" 
method (as in the example above) or from an extension method. So if `D#m()` 
would be implemented as an extension method the calling code would look like 
this:

    ...
    class D_ext {
        static void m(D self) {
            C_ext.m(self);
            System.out.println("D.m()");
        }
    }
    ...

### Extension method calling 'super' to normal method

Surprisingly things are a little more complicated if an extension methods of
class `C` does a `super.m()` call but the next implementation of 
`m()` up in the type hierarchy of `C` is a "normal" method in a base class of 
`C`, say `B`. 

Have a look at the following scenario, not yet using extension methods:

    class B {
        void m() {
            System.out.println("B.m()");
        }
    }

    class C extends B {
        void m() {
            super.m();
            System.out.println("C.m()");
        }
    }

    class E extends C {
        void m() {
            super.m();
            System.out.println("E.m()");
        }
    }

Running the following code:
    
    E e = new E();
    e.m()

will output:

    B.m()
    C.m()
    D.m()

Now assume the method `C#m()` is not a normal method but provided as an
extension method: 

    class B {
        void m() {
            System.out.println("B.m()");
        }
    }

    class C extends B {
    }

    class C_ext {
        static void m(C self) {
            // ??? super.m(); ???
            System.out.println("C.m()");
        }
    }

    class E extends C {
        void m() {
            C_ext.m(self);
            System.out.println("E.m()");
        }
    }

As you can see we changed the `super` call in `E.m()` to call the extension
method. 

The tricky part is how we translate the `super.m()` call in the `C#m()` to 
proper code in the extension method `C_ext#m(C)`. 

Recall that `super.m()` in `C#m()` must call the method `m()` of `C`'s super 
class, i.e. it must call `B#m()`. But how can we invoke `B#m()` from within
`C_ext#m(C)`? Just calling `self.m()`, as in the following snippet, will not
work:

    ...
    class C_ext {
        static void m(C self) {
            self.m();  // WILL NOT WORK
            System.out.println("C.m()");
        }
    }
    ...

For an instance of class `E` this will lead to an endless loop:

- in `C_ext#m(C)`  `self` is an `E`
- class `E` implements `m()`, i.e.  `self.m()` will call `E#m()`
- `E#m()` calls `C_ext#m(C)`
- --> endless loop

As it turns out there is no way to directly pass control to `B#m()` from 
`C_ext#m(C)` without modifying the class `B`. But if we allow modifying `B`
we can use the following approach:

- extract the body of the method `B#m()` into a new method `B#m$class_B()`
- to do the `super` call in the extension method call `self.m$class_B()`


    class B {
        void m$class_B() {
            System.out.println("B.m()");
        }

        void m() {
            m$class_B();
        }
    }
    ...
    class C_ext {
        static void m(C self) {
            self.m$class_B();
            System.out.println("C.m()");
        }
    }
    ...

With this trick we can specify the exact implementation of `m()` we would like
to call: `m$class_B()` is the implementation of `m()` in the class `B`. 
One can look at `m$class_B()` as the qualified name of a method.
The "old" method `m()` in `B` still exists and behaves as before, as it just 
calls `m$class_B()`.

### Calling a mixture of extension methods and/or "normal" methods 

Things get more complicated when we are calling a method that may be 
implemented by one or more extension methods and may also have one or more
"normal" implementations. Depending on the concrete instance we are calling 
the method for we need to call either the extension method or the "normal" 
implementation. We already had a glimpse at a similar issue when discussing the
"Extension method calls 'super' of normal method" case.

To demonstrate the problem consider the following scenario (methods marked with
`@EM` are implemented as extension methods)

    A: abstract m();
    △  
    |
    B: m() {...}
    △ △_______________________________ G: m() {...} 
    |                                     
    C: @EM m() {...}         
    △ △ 
    | |_______________________________ F: m() {...} 
    |                                     
    D: @EM m() {...}                       
    △  
    |
    E: m() {...} 

(_Scenario: Extension and normal methods mixed_)

Assume you call method `m()` on an object `o` what code do you need to write
for this invocation? When can you use the simple 'dot' notation, and when do
you need to use the static methods in the extension classes? And on what
extension class? This all boils down to the question of 
how we deal with _polymorhism_ in our extension methods approach.

Before we go a little deeper into this issue let us review the code we 
used when calling `super` to an extension method: 

    class C_ext {
        static void m(C self) {
            System.out.println("C.m()");
        }
    }

    class D extends C {
        void m() {
            C_ext.m(self);
            System.out.println("D.m()");
        }
    }

For reasons that will become clearer in a moment let us rewrite this code in a
way very similar to what we did when we wanted to invoke a very specific,
qualified implementation of a normal method. Extract the body of `C_ext#m(C)` 
to a new method `C_ext#m$class_C(C)` and use this method instead of 
`C_ext#m(C)` for the `super` call:

    class C_ext {
        static void m$class_C(C self) {
            System.out.println("C.m()");
        }

        static void m(C self) {
            m$class_C(self);
        }
    }

    class D extends C {
        void m() {
            C_ext.m$class_C(self);
            System.out.println("D.m()");
        }
    }

These are simple refactorings that don't change the behaviour. But now we
have a way to handle the "polymorphism" in a quite elegant way.

Now back to our "Scenario: Extension and normal methods mixed". To get a better
idea what we are looking for let us ask the follwing question: assume we have
an object `o` of type `T` (with a type from the given scenario) how does the 
calling code for `m()` look like?

<table>
<tr><th>Type</th><th>Calling Code</th><th>Comment</th></tr>
<tr><td><code>G</code></td><td><code>o.m()</code></td>
    <td><p>As this is a leaf class (with no subclasses) we just look up the 
     inheritance to the root. The first implementation of <code>m()</code> we 
     find is a normal method.
     </p>
     <p>So we can use the normal "dot" invocation.</p></td></tr>
<tr><td><code>F</code></td><td><code>o.m()</code></td>
    <td>(see above)</td></tr>
<tr><td><code>E</code></td><td><code>o.m()</code></td>
    <td>(see above)</td></tr>
<tr><td><code>D</code></td><td><pre>if (o instanceof E)
    o.m();
else
    D_ext.m$class_D(o);
</pre></td>
    <td><p>When we look up the inheritance to the root the first implementation of 
     <code>m()</code> we find is the extension method in <code>D</code>. 
     </p>
     <p>As <code>D</code> has subclasses an instance of <code>D</code> could 
     actually be in instance of any of these (non-abstract) subclasses. 
     If there are any implementations of <code>m()</code> 
     between <code>D</code> and those classes we need to consider this in our 
     calling code for <code>m()</code>.
     </p>
     <p>As <code>E</code>, the only subclass of <code>D</code>,
     implements <code>m()</code> as a normal class, we need to call 
     <code>m()</code> using dot notation when the object is an instance of 
     <code>E</code>. Otherwise we call the extension method of <code>D</code>.
     </p></td></tr>
</table>

<b>Dispatching Method: </b>To make things more readable and to avoid repetition we don't use the calling 
code given for `D` directly, but we encapsulate it in a method. As it turns out
the static method `m()` in the extension class is a perfect place to contain
this code. So we end up with this implementation for `D_ext`:

    class D_ext {
    
        static void m$class_D(D self) {
            C_ext.m$class_C(self);
        }
    
        static void m(D self) {
            if (self instanceof E)
                self.m2(sb);
            else
                m$class_D(self);
        }
    }

<table>
<tr><th>Type</th><th>Calling Code</th><th>Comment</th></tr>
<tr><td><code>C</code></td><td><pre>if (o instanceof D)
    D_ext.m$class_D((D) o);
else if (o instanceof F)
    o.m();
else
    C_Ext.m$class_C(o);
</pre>
</td><td><p>Similar to <code>D</code>.</p>
<p>
In this case <code>C</code> has two direct
subclasses, both are defining their own <code>m()</code> implementations. So we
check for the subclasses and use "dot" calls for <code>F</code> instances 
(as <code>F#m()</code> is a normal method) and the extension method of 
<code>D</code> for instances o <code>D</code>. Otherwise we use the extension 
method of <code>C</code>.
</p>
<p>Similar to what we explained for class <code>D</code> the calling code for 
<code>C</code> instances goes into <code>C_Ext.m(C)</code>. 
</p>
<p>Notice: calling <code>D_ext.m$class_D((D) o)</code>
also takes care of the differentiation between <code>D</code> and 
 <code>E</code> instances.</p></td></tr>

<tr><td><code>B</code></td><td><pre>if (o instanceof C)
    C_ext.m((C) o);
else
    o.m();
</pre></td><td><p><code>B</code> has two direct
subclasses, both are defining their own <code>m()</code> implementations. And
<code>B</code> defines its own "normal" <code>m()</code> method. As the subclass
<code>C</code> defines <code>m()</code> as an extension method we use the static
call to <code>C_ext.m2(C)</code> for <code>D</code> instances. The subclass
<code>G</code> also defines <code>m()</code> as a normal method so we can use
the normal dot-notation for <code>G</code> and <code>B (non-C)</code> instances. 
As a little optimization we don't need to check for these types but handle them
in the same way.
</p>
<p>As a little difference to the cases for class <code>C</code> and <code>D</code>
no extension methods are defined for class <code>B</code>, i.e. no extension
class <code>B_ext</code> exists. However to make the code more consistent and 
also have a place to move our calling code we introduce such a class 
<code>B_ext</code> and move the calling code to <code>B_ext.m()</code>.
</p></td></tr>
<tr><td><code>A</code></td><td><code>B_ext.m(o)</code></td><td>
<p>Class <code>A</code>, an abstract class directly extending <code>Object</code>, 
defines no method <code>m()</code>. So if we need to call method 
<code>m()</code> on an instance of <code>A</code> we can delegate this task to 
<code>A</code>'s subclasses. As the only subclass of <code>A</code> is 
<code>B</code> we call the method <code>B_ext.m(o)</code>./p>
</td></tr>
</table>

## Quick Reference

Let us now summarize the approach just presented in the form of some short
rules. For details see the descriptions above.

### Definitions

#### Implementation Class of m() for C

Given a class `C` and a method (signature) `m()` the
__implemention class of m() for C__ is either `C` when `C` implements `m()`
either as a normal or an extension method, or the first base class of `C` that
implements `m()` either as a normal or an extension method. Base classes are
checked starting at the direct base class of `C`, up to `Object`.

#### Implementation of m() for C

Given a class `C` and a method (signature) `m()` the
__implemention of m() for C__ is the method `m()` in the implementation class
of `m()` from `C`.

#### Sub-Implementations of m() from C

Given a class `C` and a method (signature) `m()`. 

The __sub-implementations of m() from C__ are all implementations of `m()` in
subclasses of `C`,  either as normal or extension methods.

### Rules

#### Defining Extension Methods 

To add an extension method `m()` to class `C` 
- define the method as a static method `m$class_C(C)` in the (possibly new) class `C_ext`. 
- add an extra parameter `C self` to the method's parameters (typically at the start) 
- replace all usages of `this` in the method body with `self`.

#### Calling Extension Methods

When calling a method `m()` that may be implemented by an extension method one 
cannot always call it the normal way using dot-notation (`o.m()`) but needs to
take typing information into account.

Assume we need to call `m()` on an object `o` of type `T`. Here are the rules:

- when the implementation of `m()` for `T` is a normal method and all
  sub-implementations of `m()` from `T` are normal methods 
  - use the dot-notation `o.m()` for the invocation.
- when the implementation of `m()` for `T` is a normal method and some
  sub-implementations of `m()` from `T` are extension methods (let _EMC_ be
  the "nearest" subclasses of `T` containing an extension method `m()`):
  - define a method `T_ext.m(T)` with the following behaviour:
    - if `o` in an instance of `X` and `X` is an element of _EMC_
      - call `X_ext.m(o)`
    - otherwise
      - call `o.m()`
  - use `T_ext.m(o)` for the invocation.
- when the implementation of `m()` for `T` is an extension method in class `C`
  and there are no sub-implementations  of `m()` from `T`:
  - when `T` is `C`:
    - define a method `C_ext.m(C)` that calls `C_ext.m$class_C(self)`
    - call `C_ext.m(o)`
  - otherwise, (i.e. `T` is a subclass of `C`)
    - call `C_ext.m(o)`
- when the implementation of `m()` for `T` is an extension method in class `C`
  and there are sub-implementations  of `m()` from `T` (let _EMC_ be
  the "nearest" subclasses of `T` containing an extension method `m()` and 
  _NC_ be the "nearest" subclasses of `T` containing a normal method `m()`):
  - define a method `T_ext.m(T)` with the following behaviour:
    - if `o` in an instance of `X` and `X` is an element of _EMC_
      - call `X_ext.m(o)`
    - if `o` in an instance of `X` and `X` is an element of _NC_ and `X` is not
      an instance of any class in _EMC_
      - call `o.m()`
    - otherwise
      - when `T` is `C`:
        - call `C_ext.m$class_C(self)`
      - otherwise, (i.e. `T` is a subclass of `C`)
        - call `C_ext.m(o)`
  - use `T_ext.m(o)` for the invocation.

#### Method calling 'super' to Extension Method

When a method of class `T` contains a `super.m()` call and the first 
implementation of `m()` in the super classes of `T` is an extension method 
of class `C` 
- replace the `super.m()` call with a `C_ext.m$class_C(C)` call.

#### Extension method calling 'super' to normal method

When an extension method of class `T` contains a `super.m()` call and the first 
implementation of `m()` in the super classes of `T` is a normal method
in  class `C`:
- extract the body of method `C#m()` into a new method `C#m$class_C()`
- implement the `super` call in the extension method as `self.m$class_C()`

## Example

The source directory contains the following example, demonstrating how to use 
extension methods in Java in more context.

Assume you have the following classes, methods and inheritance:

    A: m1() {...}
    △  m1b() {...}
    |  abstract m2();
    |  
    B: m2() {m1();}
    △ △_______________________________ G: m1()  {...}
    |                                     m1b() {...}
    C:  m4() {m2(); m1b()}                m2()  {super.m2();} 
    △ △  
    | |_______________________________ F: m1()  {...} 
    |                                     m1b() {...}
    D: m1() {...}                         m2()  {super.m2();} 
    △  m3() {m2();}
    |   
    |
    E: m1()  {...}
       m1b() {...} 
       m2()  {super.m2();} 

_(For better readablity method paramters bodies are omitted most of the time.)_

Now you want to extend the classes `C` and `D` with their own implementation of
`m2()`

    C:  m2() {super.m2(); m1b();}

    D:  m2() {super.m2();} 

If you add these methods directly to the body of their classes `C` and `D`
the system would look like this:

    A: m1() {...}
    △  m1b() {...}
    |  abstract m2();
    |  
    B: m2() {m1();}
    △ △_______________________________ G: m1()  {...}
    |                                     m1b() {...}
    C:  m2() {super.m2(); m1b();}         m2()  {super.m2();} 
    △ △ m4() {m2(); m1b()} 
    | |_______________________________ F: m1()  {...} 
    |                                     m1b() {...}
    D: m1() {...}                         m2()  {super.m2();} 
    △  m2() {super.m2();} 
    |  m3() {m2();} 
    |
    E: m1()  {...}
       m1b() {...} 
       m2()  {super.m2();} 


### The Source Code

#### No Extension Methods (`noext`)

The package `org.abego.extensionmethodjava.noext` contains the classes of the 
example, using the "normal" Java approach, without extension methods.

In addition, you will find a test class `NoExtensionsTest` that verifies
the behaviour of the classes and methods in different scenarios, especially
the calling sequences.

#### With Extension Methods (`withext`)

The package `org.abego.extensionmethodjava.withext` contains the example, but
this time the methods  `C#m2(...)` and `D#m2(...)` are added via the extension
method mechanism.

To show both implementation behave the same we create a test class
`WithExtensionsTest` that is actually a copy of `NoExtensionsTest` from `noext`,
but it operates on the classes in the `...withext` package. The only other 
differences are the adapted calls to extended methods.

## TODO
- accessibility/scope
- extension class name
- (qualified) method name 
- final extension class, with private constructor


_(2021-11-07 by Udo Borkowski, under an MIT License)_