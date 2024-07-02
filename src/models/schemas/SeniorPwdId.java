package models.schemas;

public class SeniorPwdId {
    private int id;
    private String idNumber;
    private String fname;
    private String mname;
    private String lname;
    private String suffix;
    private int idType;

    public SeniorPwdId(int id, String idNumber, String fname, String mname, String lname, String suffix, int idType) {
        this.id = id;
        this.idNumber = idNumber;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.suffix = suffix;
        this.idType = idType;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getFname() {
        return fname;
    }

    public String getMname() {
        return mname;
    }

    public String getLname() {
        return lname;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getIdType() {
        return idType;
    }
}