package com.univalle.parqueaderoapp.ui.vehiculo.screen

data class AgregarVehiculoUiState (
    val tipo : TipoVehiculo? = null,
    val placa: String = "",
    val isLoading: Boolean = false,
    val errorMensaje: String? = null,
    val registroExitoso: Boolean = false,
    val placaValida: Boolean = false,
    val errorFormatoPlaca: String? = null
){
    //se muestra el campo de la placa
    val mostrarPlaca: Boolean
        get() = tipo?.requierePlaca == true

    // ¿se puede pulsar "Guardar"?
    val puedeGuardar: Boolean
      //  get() = tipo != null && !isLoading && (!tipo.requierePlaca || placa.isNotBlank())
      get() = tipo != null && !isLoading && (!tipo.requierePlaca || placaValida)
}