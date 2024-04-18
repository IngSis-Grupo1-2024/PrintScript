<h1>Welcome To PrintScript!</h1>

To communicate via command, you will have to run: 
<OL>
<LI><code>./gradlew run --args="operation fileInput version"</code>
<UL>
<LI>With this, the output will be printed in console.
<LI>operation can be: execution, validate, format, and analyzer
</UL>
<LI><code>./gradlew run --args="operation fileInput version fileOutput"</code>
<UL>
<LI>With this, the output will be written in the file output that you had choosen.
</UL>
<LI><code>./gradlew run --args="operation fileInput version fileOutput rules"</code>
<UL>
<LI>Rules for the formatter or analyzer must be chosen.
<LI>Output will be written in the chosen and specified file output.
<LI>operation can be: format or./ analyzer
</UL>
</OL>

For `sca` rules, the format should look like this:

```json
{
  "println": {
    "expression": false,
    "identifier": false,
    "literal": true
  },
  "identifier_format": {}
}
```

For `formatter` rules, the format of the file is this one:

```json
{
  "afterDeclaration": {
    "on": true,
    "quantity": 1
  },
  "beforeDeclaration": {
    "on": true,
    "quantity": 0
  },
  "assignation": {
    "on": true,
    "quantity": 2
  },
  "println": {
    "on": true,
    "quantity": 2
  }

}
```