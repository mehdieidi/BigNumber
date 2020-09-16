package com.ap.project.number;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class BigNumber {
  private static final BigNumber ZERO = new BigNumber(0);
  private static final BigNumber ONE = new BigNumber(1);
  private static final BigNumber TWO = new BigNumber(2);
  private static final BigNumber THREE = new BigNumber(3);
  private static final BigNumber FOUR = new BigNumber(4);
  private static final BigNumber FIVE = new BigNumber(5);
  private static final BigNumber SIX = new BigNumber(6);
  private static final BigNumber SEVEN = new BigNumber(7);
  private static final BigNumber EIGHT = new BigNumber(8);
  private static final BigNumber NINE = new BigNumber(9);

  private final byte[] digits;
  private boolean sign; //true means negative, false means positive


  public BigNumber(String str) {
    str = clean(str); //Removing Spaces and leading zeros

    //Validation -> Only numbers are allowed
    for(int i = 0; i < str.length(); i++) {
      if(i == 0 && str.charAt(i) == '-') { continue; } //If the number is negative it can have a - character
      if(str.charAt(i) < '0' || str.charAt(i) > '9') {
        throw new IllegalArgumentException(" Input not allowed " + str);
      }
    }

    //Detect the sign of the number
    if(str.charAt(0) == '-') {
      sign = true;
      digits = new byte[str.length() - 1]; //We dont include - character in the digits array so we dont need space for it

      int i = 0; //Index of the digits array
      for(int digitIndex = 0; digitIndex < digits.length; digitIndex++) {
        if(digitIndex == str.length() - 1) { continue; } //Skip the '-' character
        int ch = (str.charAt(str.length() - digitIndex - 1) - 48); //Store the digits in reverse order in digits array
        digits[i++] = (byte) ch;
      }
    } else {
      digits = new byte[str.length()];

      for(int digitIndex = 0; digitIndex < digits.length; digitIndex++) {
        int ch = (str.charAt(str.length() - digitIndex - 1) - 48);
        digits[digitIndex] = (byte) ch;
      }
    }
  }

  public BigNumber(long longNumber) {
    this(Long.toString(longNumber));
  }

  public BigNumber(){
    this(0);
  }


  public static BigNumber fromLong(long longNumber) {
    return new BigNumber(longNumber);
  }

  public static BigNumber fromString(String stringNumber) {
    return new BigNumber(stringNumber);
  }


  //To clean spaces before and after number & clean leading zeros
  public static String clean(String str) {
    //To remove spaces from before and after of the input number
    str = str.trim();

    //If all characters are spaces throw an exception
    if(str.length() == 0) { throw new IllegalArgumentException(" No numbers entered"); }

    //Return 0 if user entered only zeros
    int zeroCount = 0;
    for(int i = 0; i < str.length(); i++) {
      if(str.charAt(i) == '0') { zeroCount++; }
    }
    if(zeroCount == str.length()) { return "0"; }

    StringBuilder digits = new StringBuilder(str.length());

    boolean isLeadingZero = true;
    //Removing leading zeros
    for(int i = 0; i < str.length(); i++) {
      if(str.charAt(i) != '0') {
        isLeadingZero = false;
      }

      if(isLeadingZero) {
        continue;
      }

      digits.append(str.charAt(i));
    }

    return digits.toString();
  }

  //The returned string includes - character if it is a negative number
  public String toString() {
    StringBuilder strBuilder = new StringBuilder(digits.length);

    for(byte digit : digits) {
      strBuilder.append(digit);
    }

    //If the number is negative we need a - before number
    if(sign) {
      strBuilder.append('-');
    }

    return strBuilder.reverse().toString();
  }

  //The returned string does not contain - character even if the number is negative
  private String toStringWithoutDash() {
    StringBuilder strBuilder = new StringBuilder(digits.length);

    for(byte digit : digits) {
      strBuilder.append(digit);
    }

    return strBuilder.reverse().toString();
  }

  public int length() {
    return digits.length;
  }

  public boolean equals(Object object) {
    if(this == object) { return true; }
    if(object == null || getClass() != object.getClass()) { return false; }

    BigNumber number = (BigNumber) object;

    //if sign of the numbers are not same, numbers are not same
    if(sign != number.sign) { return false; }

    return Arrays.equals(digits, number.digits);
  }

  //This method includes sign of the numbers as well
  public boolean isGreaterThan(BigNumber secondNumber) {
    if(sign && secondNumber.sign) {
      if(length() > secondNumber.length()) { return false; }
      if(length() < secondNumber.length()) { return true; }

      for(int i = digits.length - 1; i >= 0; i--) {
        if(digits[i] > secondNumber.digits[i]) { return false; }
        if(digits[i] < secondNumber.digits[i]) { return true; }
      }
    }

    if(sign && !secondNumber.sign) {
      return false;
    }

    if(!sign && secondNumber.sign) {
      return true;
    }

    if(!sign) {
      return isGreaterThanWithoutSign(secondNumber);
    }

    return false;
  }

  //This method does not consider sign of the numbers
  public boolean isGreaterThanWithoutSign(BigNumber secondNumber) {
    if(length() > secondNumber.length()) { return true; }
    if(length() < secondNumber.length()) { return false; }

    for(int i = digits.length - 1; i >= 0; i--) {
      if(digits[i] > secondNumber.digits[i]) { return true; }
      if(digits[i] < secondNumber.digits[i]) { return false; }
    }

    return false;
  }

  //Compare two StringBuilder values. 1 if greater, -1 if smaller, 0 if equal
  private static int compareStringBuilder(StringBuilder str1, StringBuilder str2) {
    String str3 = str1.toString();
    String str4 = str2.toString();

    //Clean spaces and leading zeros
    str3 = clean(str3);
    str4 = clean(str4);

    //Delete all characters from str1 & str2
    str1.delete(0, str1.length());
    str2.delete(0, str2.length());

    //Append clean strings to empty str1 & str2
    str1.append(str3);
    str2.append(str4);

    //Compare str1 & str2
    if(str1.length() > str2.length()) { return 1; }
    if(str2.length() > str1.length()) { return -1; }

    for(int i = 0; i < str1.length(); i++) {
      if(str1.charAt(i) > str2.charAt(i)) { return 1; }
      if(str2.charAt(i) > str1.charAt(i)) { return -1; }
    }

    return 0; //If the strings are equal
  }

  private boolean compareArray(byte[] a, byte[] b) {
    if(a.length > b.length) { return true; }
    if(a.length < b.length) { return false; }

    for(int i = a.length - 1; i >= 0; i--) {
      if(a[i] > b[i]) { return true; }
      if(a[i] < b[i]) { return false; }
    }

    return true;
  }

  private static BigNumber random(int length) {
    Random rnd = new Random();

    StringBuilder randomNumber = new StringBuilder();

    int[] odds = {1, 3, 7, 9}; //The random number must not be an even number and must not be divisible by 5

    int digitSum = 0;
    int randomDigit;
    for (int i = 1; i <= length; i++) {
      randomDigit = rnd.nextInt(10);

      //The most significant digit cant be 0
      if (i == 1) {
        randomDigit = rnd.nextInt(9) + 1;
      }

      //The least significant digit must be one of odds arrays elements
      if (i == length) {
        randomDigit = odds[rnd.nextInt(4)];
      }

      digitSum = digitSum + randomDigit;
      randomNumber.append(randomDigit);
    }

    StringBuilder tempRandom1 = new StringBuilder();
    tempRandom1.append(randomNumber);

    StringBuilder tempRandom2 = new StringBuilder();
    tempRandom2.append(randomNumber);

    StringBuilder tempRandom3 = new StringBuilder();
    tempRandom3.append(randomNumber);

    StringBuilder tempRandom4 = new StringBuilder();
    tempRandom4.append(randomNumber);

    //Random number must not be divisible to 3, 7, 13, 17, 19
    if (digitSum % 3 == 0) { random(length); }
    if((fromString(tempRandom1.delete(randomNumber.length() - 1, randomNumber.length()).toString()).subtract(fromLong(2 * (randomNumber.charAt(randomNumber.length() - 1) - 48))).mod(SEVEN).equals(ZERO))) { random(length); }
    if((fromString(tempRandom2.delete(randomNumber.length() - 1, randomNumber.length()).toString()).add(fromLong(4 * (randomNumber.charAt(randomNumber.length() - 1) - 48))).mod(fromLong(13)).equals(ZERO))) { random(length); }
    if((fromString(tempRandom3.delete(randomNumber.length() - 1, randomNumber.length()).toString()).subtract(fromLong(5 * (randomNumber.charAt(randomNumber.length() - 1) - 48))).mod(fromLong(17)).equals(ZERO))) { random(length); }
    if((fromString(tempRandom4.delete(randomNumber.length() - 1, randomNumber.length()).toString()).add(fromLong(2 * (randomNumber.charAt(randomNumber.length() - 1) - 48))).mod(fromLong(19)).equals(ZERO))) { random(length); }

    return fromString(randomNumber.toString());
  }

  //Listing all prime numbers less than square root of the given number
  private ArrayList<BigNumber> sieve(BigNumber number) {
    int squareRootOfNumber = (int) Math.sqrt(Long.parseLong(number.toString()));

    boolean[] isNotPrime = new boolean[squareRootOfNumber + 1];

    ArrayList<BigNumber> primeNumbers = new ArrayList<>();

    for(int i = 2; i <= squareRootOfNumber; i++) {
      if(!isNotPrime[i]) {
        for(int j = i * i; j <= squareRootOfNumber; j += i) {
          isNotPrime[j] = true;
        }
      }
    }

    for(int i = 2; i <= squareRootOfNumber; i++) {
      if(!isNotPrime[i]) {
        primeNumbers.add(new BigNumber(i));
      }
    }

    return primeNumbers;
  }

  public ArrayList<BigNumber> primeFactorization() {
    //Validation -> only numbers with 9 digits are supported
    if(this.length() > 9) {
      throw new IllegalArgumentException(" Only 9 digits are supported for prime factorization");
    }

    ArrayList<BigNumber> primeFactors = new ArrayList<>();
    ArrayList<BigNumber> primeNumbers = sieve(this);

    BigNumber number = new BigNumber(this.toStringWithoutDash());

    for(BigNumber primeNumber : primeNumbers) {
      if(primeNumber.isGreaterThan(number)) {
        break;
      }

      while(number.mod(primeNumber).equals(ZERO)) {
        primeFactors.add(primeNumber);
        number = number.divide(primeNumber);
      }
    }

    if(number.isGreaterThan(ONE)) {
      primeFactors.add(number);
    }

    return primeFactors;
  }

  public BigNumber add(BigNumber secondNumber) {
    StringBuilder strBuilder = new StringBuilder();

    if(sign && !secondNumber.sign) {
      sign = false;
      BigNumber result = new BigNumber(secondNumber.subtract(this).toString());
      sign = true;
      return result;
    }

    if(!sign && secondNumber.sign) {
      secondNumber.sign = false;
      BigNumber result = new BigNumber(this.subtract(secondNumber).toString());
      secondNumber.sign = true;
      return result;
    }

    if(sign) {
      simpleAdder(secondNumber, strBuilder);

      strBuilder.append('-');

      return fromString(strBuilder.reverse().toString());
    }

    simpleAdder(secondNumber, strBuilder);

    return BigNumber.fromString(strBuilder.reverse().toString());
  }

  private void simpleAdder(BigNumber secondNumber, StringBuilder strBuilder) {
    int carrier = 0;
    for(int i = 0; i < Math.max(length(), secondNumber.length()); i++) {
      byte firstNumberDigit = i < length() ? digits[i] : 0;
      byte secondNumberDigit = i < secondNumber.length() ? secondNumber.digits[i] : 0;
      byte digitSum = (byte) (firstNumberDigit + secondNumberDigit + carrier);
      strBuilder.append(digitSum % 10);
      carrier = digitSum / 10;
    }

    if(carrier > 0) {
      strBuilder.append(carrier);
    }
  }

  public BigNumber subtract(BigNumber secondNumber) {
    StringBuilder strBuilder = new StringBuilder();

    if(!sign && !secondNumber.sign) {
      if(!compareArray(digits, secondNumber.digits)) {
        String result = '-' + secondNumber.subtract(this).toString();
        return fromString(result);
      } else {
        return simpleSubtract(secondNumber, strBuilder);
      }
    }

    if(sign && !secondNumber.sign) {
      simpleAdder(secondNumber, strBuilder);

      strBuilder.append('-');

      return fromString(strBuilder.reverse().toString());
    }

    if(sign) {
      if(compareArray(digits, secondNumber.digits)) {
        String a = '-' + simpleSubtract(secondNumber, strBuilder).toString();
        return fromString(a);
      } else {
        return secondNumber.simpleSubtract(this, strBuilder);
      }
    }

    simpleAdder(secondNumber, strBuilder);

    return fromString(strBuilder.reverse().toString());
  }

  private BigNumber simpleSubtract(BigNumber secondNumber, StringBuilder strBuilder) {
    BigNumber tempFirstNumber = new BigNumber(this.toString());

    for(int i = 0; i < Math.max(tempFirstNumber.length(), secondNumber.length()); i++) {
      byte firstNumberDigit = i < tempFirstNumber.length() ? tempFirstNumber.digits[i] : 0;
      byte secondNumberDigit = i < secondNumber.length() ? secondNumber.digits[i] : 0;

      byte digitSubtract;

      if(firstNumberDigit >= secondNumberDigit) {
        digitSubtract = (byte) (firstNumberDigit - secondNumberDigit);
        strBuilder.append(digitSubtract);
      }

      if(firstNumberDigit < secondNumberDigit) {
        firstNumberDigit += 10;
        tempFirstNumber.digits[i + 1]--;
        digitSubtract = (byte) (firstNumberDigit - secondNumberDigit);
        strBuilder.append(digitSubtract);
      }
    }

    return fromString(strBuilder.reverse().toString());
  }

  public BigNumber multiply(BigNumber secondNumber) {
    StringBuilder firstNumberStr = new StringBuilder();
    StringBuilder secondNumberStr = new StringBuilder();

    if(sign) {
      firstNumberStr.append(this.toStringWithoutDash());
    } else {
      firstNumberStr.append(this.toString());
    }

    if(secondNumber.sign) {
      secondNumberStr.append(secondNumber.toStringWithoutDash());
    } else {
      secondNumberStr.append(secondNumber.toString());
    }

    int firstNumberLength = firstNumberStr.length();
    int secondNumberLength = secondNumberStr.length();

    int[] result = new int[firstNumberLength + secondNumberLength];

    int firstNumberIndex = 0;
    int secondNumberIndex;

    for(int digit1 = firstNumberLength - 1; digit1 >= 0; digit1--) {
      int carry = 0;
      int firstNumberDigit = firstNumberStr.charAt(digit1) - '0';

      secondNumberIndex = 0;

      for(int digit2 = secondNumberLength - 1; digit2 >= 0; digit2--) {
        int secondNumberDigit = secondNumberStr.charAt(digit2) - '0';

        int sum = (firstNumberDigit * secondNumberDigit) + result[firstNumberIndex + secondNumberIndex] + carry;

        carry = sum / 10;

        result[firstNumberIndex + secondNumberIndex] = sum % 10;

        secondNumberIndex++;
      }

      if(carry > 0) {
        result[firstNumberIndex + secondNumberIndex] += carry;
      }

      firstNumberIndex++;
    }

    int i = result.length - 1;
    while(i >= 0 && result[i] == 0) {
      i--;
    }

    if(i == -1) { return ZERO; } //If there was only zeroes

    StringBuilder resultString = new StringBuilder();
    if((sign && !secondNumber.sign) || !sign && secondNumber.sign)
      resultString.append('-');

    while(i >= 0) {
      resultString.append(result[i]);
      i--;
    }

    return fromString(resultString.toString());
  }

  public BigNumber divide(BigNumber secondNumber) {
    String result;

    if(sign && !secondNumber.sign) {
      BigNumber positiveFirstNumber = new BigNumber(this.toStringWithoutDash());
      if(secondNumber.isGreaterThan(positiveFirstNumber)) {
        return BigNumber.ZERO;
      }

      result = '-' + positiveFirstNumber.simpleDivide(secondNumber).toString();
      return fromString(result);
    }

    if(!sign && secondNumber.sign) {
      BigNumber positiveSecondNumber = new BigNumber(secondNumber.toStringWithoutDash());
      if(positiveSecondNumber.isGreaterThan(this))
        return BigNumber.ZERO;

      result = '-' + this.simpleDivide(positiveSecondNumber).toString();
      return fromString(result);
    }

    if(sign) {
      BigNumber positiveFirstNumber = new BigNumber(this.toStringWithoutDash());
      BigNumber positiveSecondNumber = new BigNumber(secondNumber.toStringWithoutDash());
      if(positiveSecondNumber.isGreaterThan(positiveFirstNumber))
        return BigNumber.ZERO;

      result = positiveFirstNumber.simpleDivide(positiveSecondNumber).toString();
      return fromString(result);
    }

    return this.simpleDivide(secondNumber);
  }

  private BigNumber simpleDivide(BigNumber secondNumber) {
    //Validation -> If second number is 0, the division is meaningless
    if(secondNumber.equals(ZERO)) {
      throw new ArithmeticException(" Second operand is 0");
    }

    //If second number is larger than first number, result of division is 0
    if(secondNumber.isGreaterThan(this)) {
      return ZERO;
    }

    String firstNumberStr = this.toString();
    String secondNumberStr = secondNumber.toString();
    StringBuilder secondNumberStrBuilder = new StringBuilder(secondNumber.toString());
    StringBuilder portionStr;

    int index; //Index of digits from first number which have been participated in multiplication

    //Pick exact portion of second number from first number from left
    //if the portion is still smaller than second number, pick one more digit
    if(firstNumberStr.substring(0, secondNumber.length()).compareTo(secondNumberStr) >= 0) {
      portionStr = new StringBuilder(firstNumberStr.substring(0, secondNumber.length()));
      index = secondNumber.length() - 1;
    } else {
      portionStr = new StringBuilder(firstNumberStr.substring(0, secondNumber.length() + 1));
      index = secondNumber.length();
    }

    //Make temporary big number from picked portion
    BigNumber portionBigNumber = new BigNumber(portionStr.toString());

    StringBuilder result = new StringBuilder();

    while(index < firstNumberStr.length()) {
      int i = 1;
      BigNumber multiplier;
      while(i <= 9) {
        multiplier = new BigNumber(i);
        if(secondNumber.multiply(multiplier).isGreaterThan(portionBigNumber)) {
          result.append(--i);
          break;
        }
        i++;
      }

      if(i == 10) {
        multiplier = new BigNumber(--i);
        result.append(i);
      } else {
        multiplier = new BigNumber(i);
      }

      portionStr.delete(0, index + 1);
      portionStr.append((portionBigNumber.subtract(secondNumber.multiply(multiplier))).toString());

      if(index < firstNumberStr.length() - 1) {
        portionStr.append(firstNumberStr.charAt(index + 1));
        index++;
      } else { index++; }

      while(compareStringBuilder(portionStr, secondNumberStrBuilder) < 0 && index < firstNumberStr.length() - 1) {
        portionStr.append(firstNumberStr.charAt(index + 1));
        result.append(0);
        index++;
      }

      portionBigNumber = new BigNumber(portionStr.toString());
    }

    return fromString(result.toString());
  }

  public BigNumber mod(BigNumber secondNumber) {
    return this.subtract(this.divide(secondNumber).multiply(secondNumber));
  }

  public BigNumber pow(BigNumber secondNumber) {
    //Validation -> If exponent is negative throw exception
    if(secondNumber.sign) {
      throw new IllegalArgumentException(" Exponent can't be negative");
    }

    BigNumber tempSecondNumber = new BigNumber(secondNumber.toString());

    BigNumber result = new BigNumber(1);

    while(tempSecondNumber.isGreaterThanWithoutSign(BigNumber.ZERO)) {
      result = result.multiply(this);
      tempSecondNumber = tempSecondNumber.subtract(BigNumber.ONE);
    }

    return result;
  }

  public boolean isEven() {
    return this.mod(TWO).equals(ZERO);
  }

  public boolean isPrime(int accuracy) {
    if(!this.isGreaterThan(ONE)) {
      return false;
    }

    if(this.equals(TWO)) {
      return true;
    }

    if(this.mod(TWO).equals(ZERO)) {
      return false;
    }

    BigNumber numberMinusOne = this.subtract(ONE);

    while(numberMinusOne.isEven()) {
      numberMinusOne = numberMinusOne.divide(TWO);
    }

    for(int i = 0; i < accuracy; i++) {
      if(!rabinMiller(numberMinusOne, this)) {
        return false;
      }
    }

    return true;
  }

  private static boolean rabinMiller(BigNumber d, BigNumber number) {
    BigNumber numberMinusOne = number.subtract(ONE);

    Random rnd = new Random();
    BigNumber randomLongNumber = new BigNumber(Math.abs(rnd.nextLong()));
    BigNumber randomNumberInRange = randomLongNumber.mod(number.subtract(FOUR)).add(TWO); //Random number must be in range of (2, number - 2)

    BigNumber modPowResult = modPow(randomNumberInRange, d, number); //(randomNumberInRange ^ d) % number

    if(modPowResult.equals(ONE) || modPowResult.equals(numberMinusOne)) {
      return true;
    }

    while(!d.equals(numberMinusOne)) {
      modPowResult = modPowResult.multiply(modPowResult).mod(number);
      d = d.multiply(TWO);

      if(modPowResult.equals(ONE)) {
        return false;
      }

      if(modPowResult.equals(numberMinusOne)) {
        return true;
      }
    }

    return false;
  }

  private static BigNumber modPow(BigNumber base, BigNumber exponent, BigNumber number) {
    BigNumber result = ONE;

    base = base.mod(number);

    while(exponent.isGreaterThan(ZERO)) {
      if(!exponent.isEven()) {
        result = (result.multiply(base)).mod(number);
      }

      exponent = exponent.divide(TWO);
      base = (base.multiply(base)).mod(number);
    }

    return result;
  }

  public static BigNumber primeGenerator(int length) {
    while(true) {
      BigNumber randomNumber = random(length);

      if(randomNumber.isPrime(10)) {
        return randomNumber;
      }
    }
  }
}