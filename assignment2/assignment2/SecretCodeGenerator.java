package assignment2;

import java.util.Random;

public class SecretCodeGenerator
{
  private static SecretCodeGenerator instance = new SecretCodeGenerator();
  private Random randomGenerator;
  
  public static SecretCodeGenerator getInstance()
  {
    return instance;
  }
  
  private SecretCodeGenerator()
  {
    this.randomGenerator = new Random();
  }
  
  public String getNewSecretCode()
  {
    String str = "";
    int j = 4;
    String[] arrayOfString = GameConfiguration.colors;
    for (int k = 0; k < j; k++)
    {
      int i = this.randomGenerator.nextInt(arrayOfString.length);
      str = str + arrayOfString[i];
    }
    return str;
  }
}
