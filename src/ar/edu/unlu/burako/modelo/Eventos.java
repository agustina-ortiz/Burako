package ar.edu.unlu.burako.modelo;

public enum Eventos {
    //estados del juego
    ESPERANDO_JUGADORES,
    LISTO_PARA_INICIAR,
    JUEGO_INICIADO,
    TURNO_JUGADOR,
    PARTIDA_TERMINADA,

    //acciones de los jugadores
    JUGADOR_UNIDO,
    CARTA_TOMADA,
    CARTA_DESCARTADA,
    JUEGO_BAJADO,
    CARTA_APOYADA,
    CAMBIO_TURNO,
    MANO_CERRADA,
    GANADOR_ENCONTRADO,
}
