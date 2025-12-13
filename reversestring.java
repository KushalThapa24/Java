class ReverseString {
    public static void main(String[] args)
    {
        String str1= "Hello World";
        String str2= "";

        for(int i=str1.length() ; i>0 ; i--)
        {
            str2 = str2 + str1.charAt(i-1);
        }
        System.out.printf("The reverse of string %s is %s",str1, str2);
    }
}