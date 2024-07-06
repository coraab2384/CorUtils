<h1>CorUtils</h1>

<h3>Widely-applicable utility classes and static function sets</h3>

<p>The top-level package, corutils, contains additional functions for arrays and strings,
as well as a set of functions for dealing with possibly-null arguments.</p>

<p>The functional package contains more functional interfaces, to complement some functions that
<a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/package-summary.html">java.util.function</a>
is missing.</p>

<p>The ternary package contains ternary (three-valued-logic) related classes
<a href="https://www.wikipedia.org/wiki/Three-valued_logic">(relevant wikipedia)</a>.
Two of the noteworthy classes are:</p><ul>
<li>The Ternary class, which allows for a 3-valued boolean, with a middle default/unknown/null value.</li>
<li>Signum allows representing the results of a signum function with an enum,
rather than an int, for better switching.</li></ul>
