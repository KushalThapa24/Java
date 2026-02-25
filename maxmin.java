class MaxMin {
    public static void main (String[] args)
    {
        int[] num= {12,47,58,32,56,89,14,7};
        int max= num[0];
        int min= num[0];

        for(int i=1; i<num.length; i++)
        {
            if(num[i]>max)
            {
                max=num[i];
            }
            if(num[i]<min)
            {
                min=num[i];
            }
        }
        System.out.printf("The maximum number is %d and minimum number is %d%n", max, min);
    }
}