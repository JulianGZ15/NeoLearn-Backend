package com.julian.neolearn.neolearn.requestandresponse;


import com.julian.neolearn.neolearn.entity.MensajeChat.TipoMensaje;

public class EnviarMensajeRequest {
    private String contenido;
    private TipoMensaje tipoMensaje = TipoMensaje.TEXTO;

    // Getters y setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public TipoMensaje getTipoMensaje() { return tipoMensaje; }
    public void setTipoMensaje(TipoMensaje tipoMensaje) { this.tipoMensaje = tipoMensaje; }
}


