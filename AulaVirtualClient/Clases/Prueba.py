import sys
import glob
sys.path.append('gen-py')
import json

import threading
import PyQt5
from aulavirtual import servicios
from PyQt5.QtGui import QBrush
from PyQt5.QtGui import QPalette
from PyQt5.QtGui import QPixmap
from PyQt5.QtGui import QColor
from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QDesktopWidget
from PyQt5.QtWidgets import QLabel
from PyQt5.QtWidgets import QListView
from PyQt5.QtWidgets import QMainWindow, QMdiSubWindow
from PyQt5.QtWidgets import QMessageBox
from PyQt5.QtWidgets import QTextEdit
from PyQt5.QtWidgets import QLineEdit
from PyQt5.QtWidgets import QListWidget
from PyQt5.QtGui import QStandardItem
from PyQt5.QtGui import QStandardItemModel
from PyQt5.QtWidgets import QPushButton
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from Server import MiHilo

from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtWebEngineWidgets import *
from PyQt5.QtWebEngineWidgets import QWebEngineView, QWebEngineSettings
from PyQt5.QtWidgets import QApplication, QWidget, QMainWindow

class Prueba(QMainWindow):
    _singleton = None

    @classmethod
    def get_instance(cls, *args, **kwargs):
        if not cls._singleton:
            cls._singleton = Prueba()
        return cls._singleton

    def __init__(self):
        super().__init__()

        self.log = {}
        self.hMiHilo = MiHilo()
        self.hMiHilo.start()
        self.hMiHilo.signal_recive.connect(self.recibirMensaje)
        self.hMiHilo.signal_connected.connect(self.actualizarListaUsuarios)
        # hMiHilo.finished.connect(self.recibirMensaje)
        self.initUI()
        self.show()




    def initUI(self):
        # Add core elements for the window
        self.lblDesign = QLabel("", self)
        self.lblDesign.setStyleSheet(
            "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.lblDesign.setMinimumSize(1000, 70)

        self.img_logo = QLabel(self)
        pixmap = QPixmap("Images/mini_Teacher3.png")
        self.img_logo.setPixmap(pixmap)
        self.img_logo.move(20, -12)
        self.img_logo.setMinimumSize(100, 100)

        self.lbl_tittle1 = QLabel("Aula ", self)
        self.lbl_tittle1.setStyleSheet(
            "font-weight: b  old; color: white; font-family: century gothic; font-size: 32px")
        self.lbl_tittle1.move(415, 90)

        self.lbl_tittle2 = QLabel("Virtual", self)
        self.lbl_tittle2.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 32px")
        self.lbl_tittle2.setMinimumSize(110, 30)
        self.lbl_tittle2.move(507, 90)

        # This window
        # self.setFixedSize(1366, 768)
        self.setFixedSize(1000, 700)
        self.setWindowTitle('Aula virtual')
        palette = QPalette()

        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))

        # # Slides
        # self.lbl_slides = QLabel("", self)
        # self.lbl_slides.setStyleSheet(
        #     "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        # self.lbl_slides.setMinimumSize(645, 355)
        # self.lbl_slides.move(293, 140)

        # Camera
        self.lbl_camera = QVBoxLayout()

        self.web = QWebEngineView(self)
        settings = QWebEngineSettings.globalSettings().setAttribute(QWebEngineSettings.PluginsEnabled, True)
        settings = QWebEngineSettings.globalSettings().setAttribute(QWebEngineSettings.AllowRunningInsecureContent, True)
        self.web.page().featurePermissionRequested.connect(self.permisos)
        self.web.page().setUrl(QUrl("http://192.168.43.105:5080/demos/simpleSubscriber.html"))
        # self.web.load(QUrl("http://localhost:5080/demos/simpleSubscriber.html"))
        self.web.setMinimumSize(370,300)
        self.web.move(60,140)
        self.web.show()



        self.lbl_camera.addWidget(self.web)

        # self.lbl_camera.setStyleSheet(
        #     "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.lbl_camera

        # self.lbl_camera.
        # Chat
        self.lbl_chat = QLabel("", self)
        self.lbl_chat.setStyleSheet(
            "background-color: White; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.lbl_chat.setMinimumSize(645, 220)
        self.lbl_chat.move(293, 460)

        self.txt_messages = QTextEdit("", self)
        self.txt_messages.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 16px")
        self.txt_messages.setMinimumSize(630, 130)
        self.txt_messages.move(300, 477)
        self.txt_messages.setReadOnly(True)

        # Text field
        self.txt_message = QTextEdit("", self)
        self.txt_message.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 16px")
        self.txt_message.setMinimumSize(520, 55)
        self.txt_message.move(300, 615)

        # Send button
        self.btn_send = QPushButton('Enviar', self)
        self.btn_send.move(830, 635)
        self.btn_send.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_send.clicked.connect(self.click)


        # Student's list
        self.lbl_list = QLabel("", self)
        self.lbl_list.setStyleSheet(
            "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.lbl_list.setMinimumSize(190, 19)
        self.lbl_list.move(60, 510)

        self.lbl_last_name_m = QLabel("Alumnos conectados", self)
        self.lbl_last_name_m.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_last_name_m.setMinimumSize(190, 25)
        self.lbl_last_name_m.move(70, 510)

        self.list = QListWidget(self)
        self.list.setMinimumSize(190, 140)
        self.list.move(60, 540)

        self.model = QStandardItemModel(self.list)

        # Set default items

        self.list.show()

        # Participation button
        self.btn_participate = QPushButton('Pedir participacion', self)
        self.btn_participate.move(60, 460)
        self.btn_participate.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_participate.setMinimumSize(190, 30)
        # self.btn_participate.clicked.connect(self.btn_participate)

        # Log out button
        self.lbl_logout = QLabel('Cerrar sesión', self)
        self.lbl_logout.move(830, 40)
        self.lbl_logout.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")
        self.lbl_logout.setMinimumSize(100, 15)
        # self.lbl_clase = QLabel(self.clase["CLASE"], self)
        # self.lbl_clase.move(110, 40)
        # self.lbl_clase.setStyleSheet(
        #     "font-weight: bold; color: white; font-family: century gothic; font-size: 16px")
        # self.lbl_clase.setMinimumSize(350, 15)
        # self.btn_logout.clicked.connect(self.btn_participate)


        # QListView para la lista de alumnos
        # self.client.enviarMensaje("Hola")
        # 2F4D6B"



        # Connect!

        self.setPalette(palette)

        self.show()

    def recibirMensaje(self,mensaje):
        self.txt_messages.append(mensaje)
        print("Señal recibida")

    def actualizarListaUsuarios(self,conectados):
        self.list.clear()
        alumnos = conectados
        for alumno in alumnos:
            self.list.addItem(alumno)
            # item = QStandardItem(alumno)
            # self.model.appendRow(item)

        # self.list.setModel(self.model)
        print("Señal recibida")


    def click(self):
        transport2 = TSocket.TSocket('localhost', 9090)

        # Buffering is critical. Raw sockets are very slow
        transport2 = TTransport.TBufferedTransport(transport2)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport2)

        # Create a client to use the protocol encoder
        client = servicios.Client(protocol)
        transport2.open()

        # handler = self
        # processor = servicios.Processor(handler)
        # transport2 = TSocket.TServerSocket(port=1010)
        # tfactory = TTransport.TBufferedTransportFactory()
        # pfactory = TBinaryProtocol.TBinaryProtocolFactory()
        # server = TServer.TSimpleServer(processor, transport2, tfactory, pfactory)
        # server.serve()

        # hMiHilo = MiHilo()
        # hMiHilo.start()


        self.msg = self.txt_message.toPlainText()
        print(self.msg)
        client.entrarAula("Susana","localhost","IA")

        client.enviarMensaje(self.msg)

        transport2.close()

        print("Salida")

    def permisos(self, url, feature):
        self.web.page().setFeaturePermission(QUrl("http://localhost:5080/demos/simpleBroadcaster.html"),
                                        QWebEnginePage.MediaAudioVideoCapture, QWebEnginePage.PermissionGrantedByUser)



if __name__ == '__main__':
    app = QApplication(sys.argv)
    # try:

    # except Thrift.TException as tx:
    #     print(tx.message)

    # app = QApplication(sys.argv)

    prueba = Prueba()




    sys.exit(app.exec_())





