/**
 * <b>Method Extensions</b>
 * <p>
 * Demonstrates how to use "method extensions".
 * <p>
 * The classes in "WithExtensions" correspond to the classes in "NoExtensions",
 * but with the methods C#m2() and D#m2() not implemented directly in their
 * classes, but through "Extension" classes.
 * <p>
 * The examples demonstrate the following cases:
 * <table>
 *     <tr><td>method</td><td>calls</td><td>extension method</td><td><code>D.m3() -> D.m2()</code></td></tr>
 *     <tr><td>normal method</td><td>super calls</td><td>extension method</td><td><code>E.m2() -> D.m2()</code></td></tr>
 *     <tr><td>extension method</td><td>super calls</td><td>extension method</td><td><code>D.m2() -> C.m2()</code></td></tr>
 *     <tr><td>extension method</td><td>super calls</td><td>normal method</td><td><code>C.m2() -> B.m2()</code></td></tr>
 *     <tr><td>normal method</td><td>calls</td><td>normal (overridden) method</td><td><code>B.m2() -> m1()</code></td></tr>
 *     <tr><td>extension method</td><td>calls</td><td>normal (overridden) method</td><td><code>C.m2() -> m1b()</code></td></tr>
 * </table>
 * <p>
 * These examples operate on "instances", but the same principles apply when
 * working on "class level", as classes are also "instances" (of their
 * meta class).
 */
package org.abego.lab.methodextension.withext;