package com.rodriguez.sipap;

public class Transferencia {
    private String cuenta;
    private int monto;
    private String bancoOrigen;
    private String bancoDestino;

    public Transferencia() {}

    public Transferencia(String cuenta, int monto, String bancoOrigen, String bancoDestino) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.bancoOrigen = bancoOrigen;
        this.bancoDestino = bancoDestino;
    }

    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }

    public int getMonto() { return monto; }
    public void setMonto(int monto) { this.monto = monto; }

    public String getBancoOrigen() { return bancoOrigen; }
    public void setBancoOrigen(String bancoOrigen) { this.bancoOrigen = bancoOrigen; }

    public String getBancoDestino() { return bancoDestino; }
    public void setBancoDestino(String bancoDestino) { this.bancoDestino = bancoDestino; }

    @Override
    public String toString() {
        return "{" +
                "\"cuenta\":\"" + cuenta + "\"," +
                "\"monto\":" + monto + "," +
                "\"banco_origen\":\"" + bancoOrigen + "\"," +
                "\"banco_destino\":\"" + bancoDestino + "\"" +
                "}";
    }
}
