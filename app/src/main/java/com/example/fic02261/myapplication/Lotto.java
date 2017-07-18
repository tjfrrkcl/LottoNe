package com.example.fic02261.myapplication;

/**
 * Created by FIC02261 on 2016-09-21.
 */
class Lotto {
    private int recu_no;
    private String com_yn;
    private int k1;
    private int k2;
    private int k3;
    private int k4;
    private int k5;
    private int k6;

    public Lotto() {
    }

    public Lotto(int recu_no) {
        this.recu_no = recu_no;
    }

    public Lotto(int recu_no, String com_yn, int k1, int k2, int k3, int k4, int k5, int k6) {
        this.recu_no = recu_no;
        this.com_yn = com_yn;
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.k4 = k4;
        this.k5 = k5;
        this.k6 = k6;
    }

    public int getK1() {
        return k1;
    }

    public void setK1(int k1) {
        this.k1 = k1;
    }

    public int getK2() {
        return k2;
    }

    public void setK2(int k2) {
        this.k2 = k2;
    }

    public int getK3() {
        return k3;
    }

    public void setK3(int k3) {
        this.k3 = k3;
    }

    public int getK4() {
        return k4;
    }

    public void setK4(int k4) { this.k4 = k4; }

    public int getK5() {
        return k5;
    }

    public void setK5(int k5) { this.k5 = k5; }

    public int getK6() {
        return k6;
    }

    public void setK6(int k6) { this.k6 = k6; }

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
