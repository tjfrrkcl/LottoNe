package com.example.fic02261.myapplication;

/**
 * Created by FIC02261 on 2016-09-21.
 */
class Lotto {
    private int recu_no;
    private String com_yn;
    private int f1;
    private int s2;
    private int t3;
    private int f4;
    private int f5;
    private int s6;

    public Lotto() {
    }

    public Lotto(int recu_no) {
        this.recu_no = recu_no;
    }

    public Lotto(int recu_no, String com_yn, int f1, int s2, int t3, int f4, int f5, int s6) {
        this.recu_no = recu_no;
        this.com_yn = com_yn;
        this.f1 = f1;
        this.s2 = s2;
        this.t3 = t3;
        this.f4 = f4;
        this.f5 = f5;
        this.s6 = s6;
    }

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getS2() {
        return s2;
    }

    public void setS2(int s2) {
        this.s2 = s2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getF4() {
        return f4;
    }

    public void setF4(int f4) {
        this.f4 = f4;
    }

    public int getF5() {
        return f5;
    }

    public void setF5(int f5) {
        this.f5 = f5;
    }

    public int getS6() {
        return s6;
    }

    public void setS6(int s6) {
        this.s6 = s6;
    }

    public int getRecu_no() {
        return recu_no;
    }

    public void setRecu_no(int recu_no) {
        this.recu_no = recu_no;
    }

    public String getCom_yn() {
        return com_yn;
    }

    public void setCom_yn(String com_yn) {
        this.com_yn = com_yn;
    }
}
