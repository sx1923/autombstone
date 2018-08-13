# autombstone
Auto save and restore ui data framework for Android.
自动实现UI数据的保存和恢复

使用方法 ：

在Activity或Fragment中，将需要进行自动数据恢复的变量添加@SafeUiData注解即可 


    @SafeUiData
    String strTestValue;
    @SafeUiData
    int intTestValue;
    @SafeUiData
    Student student;
    @SafeUiData
    ArrayList<String> strList;
    @SafeUiData
    ArrayList<Student> studentList;
    @SafeUiData
    HashMap<String, Student> map;
    @SafeUiData
    Teacher teacher;


框架原理类似butterknife的APT思路，供参考。
