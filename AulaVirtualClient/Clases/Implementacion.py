import sys
import glob
sys.path.append('gen-py')
import json
from aulavirtual import servicios

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
# from Prueba import Prueba
from PyQt5.QtCore import pyqtSignal
from PyQt5.QtCore import QObject

class Implementacion(QObject):
    signal_recive = pyqtSignal(str)
    signal_connected = pyqtSignal(list)
    signal_ask_participation = pyqtSignal()
    signal_activate_participation=pyqtSignal()
    signal_desactivate_participation = pyqtSignal()
    signal_show_solicitud = pyqtSignal(list)
    signal_get_control= pyqtSignal()
    signal_leave_control = pyqtSignal()

    def recibirMensaje(self, mensaje):
        print(mensaje)
        self.signal_recive.emit(mensaje)

    def actualizarUsuariosConectados(self, conectados):
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
