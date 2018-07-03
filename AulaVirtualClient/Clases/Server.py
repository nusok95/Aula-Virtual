# Libreria para el manejo de los hilos
import threading
from aulavirtual import servicios
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from Implementacion import Implementacion

from PyQt5.QtCore import pyqtSignal
from PyQt5.QtCore import QThread

# class MiHilo(threading.Thread):
    # def __init__(self):
    #     threading.Thread.__init__(self)
    #     self.stoprequest = threading.Event()

class MiHilo(QThread):
    signal_recive = pyqtSignal(str)
    signal_connected = pyqtSignal(list)
    signal_ask_participation = pyqtSignal()
    signal_activate_participation = pyqtSignal()
    signal_desactivate_participation = pyqtSignal()
    signal_show_solicitud = pyqtSignal(list)
    signal_get_control = pyqtSignal()
    signal_leave_control = pyqtSignal()

    _singleton = None

    @classmethod
    def get_instance(cls, *args, **kwargs):
        if not cls._singleton:
            cls._singleton = MiHilo()
        return cls._singleton

    def run(self):
        print("Servidor corriendo")
        handler = Implementacion()
        # handler.signal_recive.connect(lambda mensaje: self.emitir(mensaje))
        handler.signal_recive.connect(self.recibir_mensaje)
        handler.signal_connected.connect(self.recibir_conectados)
        handler.signal_activate_participation.connect(self.activarSolicitudParticipacion)
        handler.signal_desactivate_participation.connect(self.desactivarSolicitudParticipacion)
        handler.signal_show_solicitud.connect(self.mostrarSolicitudParticipacion)
        handler.signal_get_control.connect(self.obtenerControl)
        handler.signal_leave_control.connect(self.dejarControl)
        processor = servicios.Processor(handler)
        transport = TSocket.TServerSocket(port=9091)
        tfactory = TTransport.TBufferedTransportFactory()
        pfactory = TBinaryProtocol.TBinaryProtocolFactory()
        server = TServer.TThreadedServer(processor, transport, tfactory, pfactory)
        server.serve()

        print("servidor corriendo...")


    def recibir_mensaje(self,mensaje):
        self.signal_recive.emit(mensaje)
        self.mensaje= mensaje
        print("EL MENSAJE ES: "+self.mensaje)

    def recibir_conectados(self,conectados):
        self.signal_connected.emit(conectados)

    def pedirParticipacion(self):
        self.signal_ask_participation.emit()

    def activarSolicitudParticipacion(self):
        self.signal_activate_participation.emit()

    def desactivarSolicitudParticipacion(self):
        self.signal_desactivate_participation.emit()

    def mostrarSolicitudParticipacion(self, datosAlumno):
        self.signal_show_solicitud.emit(datosAlumno)

    def obtenerControl(self):
        self.signal_get_control.emit()

    def dejarControl(self):
        self.signal_leave_control.emit()


    def stop(self):
        self._isRunning = False







