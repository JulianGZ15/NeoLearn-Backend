package com.julian.neolearn.neolearn.requestandresponse;

import java.util.List;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;

public class HistorialChatResponse {
    private List<MensajeChatDTO> mensajes;
    private Integer totalMensajes;
    private Integer pagina;
    private Boolean hayMasPaginas;

    // Constructor
    public HistorialChatResponse(List<MensajeChatDTO> mensajes, Integer totalMensajes, Integer pagina, Boolean hayMasPaginas) {
        this.mensajes = mensajes;
        this.totalMensajes = totalMensajes;
        this.pagina = pagina;
        this.hayMasPaginas = hayMasPaginas;
    }

    // Getters y setters
    public List<MensajeChatDTO> getMensajes() { return mensajes; }
    public void setMensajes(List<MensajeChatDTO> mensajes) { this.mensajes = mensajes; }

    public Integer getTotalMensajes() { return totalMensajes; }
    public void setTotalMensajes(Integer totalMensajes) { this.totalMensajes = totalMensajes; }

    public Integer getPagina() { return pagina; }
    public void setPagina(Integer pagina) { this.pagina = pagina; }

    public Boolean getHayMasPaginas() { return hayMasPaginas; }
    public void setHayMasPaginas(Boolean hayMasPaginas) { this.hayMasPaginas = hayMasPaginas; }
}
