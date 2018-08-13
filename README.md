# autombstone
Auto save and restore ui data framework for Android.
自动实现UI数据的保存和恢复

使用方法 ：

在Activity或Fragment中，将需要进行自动数据恢复的变量添加@SafeUiData注解即可 


    @SafeUiData
    public String mStrTestValue;
    @SafeUiData
    public int mIntTestValue;
    @SafeUiData
    public Student mStudent;
    @SafeUiData
    public ArrayList<String> mStrList;
    @SafeUiData
    public ArrayList<Student> mStudentList;
    @SafeUiData
    public HashMap<String, Student> mMap;
    @SafeUiData
    public Teacher mTeacher;
