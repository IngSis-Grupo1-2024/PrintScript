<h1>Welcome To PrintScript!</h1>

To communnicate via command, you will have to run: 
<OL>
<LI><code>./gradlew run --args="operation fileInput version"</code>
<UL>
<LI>With this the output will be printed in console.
<LI>operation can be: execution, validate, format, and analyzer
</UL>
<LI><code>./gradlew run --args="operation fileInput version fileOutput"</code>
<UL>
<LI>With this the output will be writted in the file output that you had choosen.
</UL>
<LI><code>./gradlew run --args="operation fileInput version fileOutput rules"</code>
<UL>
<LI>With this you must choose the rules for the formatter or analyzer.
<LI>With this the output will be writted in the file output that you had choosen.
<LI>operation can be: format and analyzer
</UL>
</OL>
