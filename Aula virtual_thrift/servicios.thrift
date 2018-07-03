namespace java aulavirtual
namespace py aulavirtual

service servicios{
	string iniciarSesion(1:string username, 2:string password);

	string registrarse(1:string matricula, 2:string nombre, 3:string apellidoPaterno, 
		4:string apellidoMaterno, 5:string correo, 6:string password, 7:i32 tipoUsuario);

	list<string> obtenerClasesUsuario(1:string matricula);

	list<string> obtenerMaestros();

	list<string> obtenerClases();

	list<string> obtenerPeriodos();

	string registrarClase(1:string NRC, 2:string nombreClase, 3:i32 idPeriodo, 4:string numeroPersonalMaestro);

	string inscribirClase(1:string matricula, 2:string NRC, 3:string rol);

	void enviarMensaje(1:string mensaje, 2:string clase);

	void recibirMensaje(1:string mensaje);

	void pedirParticipacion(1:string nombre, 2:string ip);

	void mostrarSolicitudParticipacion(1:list<string> datosAlumno)

	void otorgarParticipacion(1:string ipAlumno);


	void mostrarParticipacion(1:string ipAlumno);

	void activarSolicitudParticipacion();

	void desactivarSolicitudParticipacion(); 

	void pedirControl(1:string ipAlumno);

	void obtenerControl();
	void dejarControl();

	void recuperarControl(1:string ipAlumno);

	void salirAula(1:string nombre, 2:string ip, 3:string clase, 4:i32 rol);

	void entrarAula(1:string nombre, 2:string ip, 3:string clase, 4:i32 rol);

	void actualizarUsuariosConectados(1:list<string> conectados);

	void actualizarParticipantes();

	
}