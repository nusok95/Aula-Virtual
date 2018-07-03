import sys
import glob
sys.path.append('gen-py')
import json
from aulavirtual import servicios
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtWebEngineWidgets import *
from PyQt5.QtWebEngineWidgets import QWebEngineView, QWebEngineSettings
from PyQt5.QtWidgets import QApplication, QWidget, QMainWindow

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

import socket




class WindowClassroom(QMainWindow):
    URL_TEACHER="http://localhost:5080/demos/simpleBroadcaster.html"
    URL_STUDENT="http://192.168.43.2:5080/demos/simpleSubscriber.html"

    def __init__(self,client,transport,clase,usuario,nombre):
        super().__init__()
        self.btn_participate = QPushButton('Pedir participacion', self)
        self.setVisible(False)
        self.client=client
        self.transport=transport
        self.clase=clase
        self.usuario=usuario
        self.nombre_usuario = nombre
        self.hMiHilo= MiHilo.get_instance()
        self.hMiHilo.start()
        self.hMiHilo.signal_recive.connect(self.recibirMensaje)
        self.hMiHilo.signal_connected.connect(self.actualizarListaUsuarios)
        self.hMiHilo.signal_activate_participation.connect(self.activar_btn_participacion)
        self.hMiHilo.signal_desactivate_participation.connect(self.desactivar_btn_participacion)
        self.hMiHilo.signal_show_solicitud.connect(self.show_reques)
        self.hMiHilo.signal_get_control.connect(self.tomar_control_video)
        self.hMiHilo.signal_leave_control.connect(self.ceder_control_video)
        # self.hMiHilo.signal_show_solicitud.connect(self.)
        # self.client=client
        # self.transport=transport

        self.initUI()
        self.show()


    def initUI(self):
        # Add core elements for the window
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
        settings = QWebEngineSettings.globalSettings().setAttribute(QWebEngineSettings.AllowRunningInsecureContent,
                                                                    True)
        self.web.page().featurePermissionRequested.connect(self.permisos)
        self.iniciar_video()
        # self.web.page().setUrl(QUrl("http://localhost:5080/demos/simpleSubscriber.html"))
        # self.web.load(QUrl("http://localhost:5080/demos/simpleSubscriber.html"))
        self.web.setMinimumSize(370, 300)
        self.web.move(60, 140)
        self.web.show()

        self.lbl_camera.addWidget(self.web)

        self.lbl_slides = QVBoxLayout()

        self.web_slides = QWebEngineView(self)
        print(self.clase["CARPETA_COMPARTIDA"])
        self.web_slides.page().setUrl(QUrl(self.clase["CARPETA_COMPARTIDA"]))
        # self.web_slides.setEnabled(False)
        self.web_slides.setMinimumSize(480, 300)
        self.web_slides.move(450, 140)
        self.web_slides.show()

        self.lbl_slides.addWidget(self.web_slides)

        # self.lbl_camera.setStyleSheet(
        #     "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")


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
        self.btn_send.clicked.connect(self.click_send)

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

        # self.model = QStandardItemModel(self.list)
        #
        # #Set default items
        # alumnos = ['Ana Paola', 'Susana', 'Erasmo Carlos']
        # for alumno in alumnos:
        #     item = QStandardItem(alumno)
        #     self.model.appendRow(item)
        #
        # self.list.setModel(self.model)
        self.list.show()
        self.iniciar_conectados()

         

        #Participation button
        self.cargar_botones()

         #Log out button
        self.lbl_logout = QLabel('Cerrar sesión', self)
        self.lbl_logout.move(830, 40)
        self.lbl_logout.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")

        self.lbl_logout.setMinimumSize(100, 15)
        self.lbl_logout.mousePressEvent = self.click_exit






        self.lbl_clase = QLabel(self.clase["CLASE"], self)
        self.lbl_clase.move(110, 40)
        self.lbl_clase.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 16px")
        self.lbl_clase.setMinimumSize(350, 15)


        # 2F4D6B"
        self.setPalette(palette)
        self.show()


    def iniciar_video(self):
        if self.clase["ROL"]=="Alumno":
            self.web.page().setUrl(QUrl(self.URL_STUDENT))
        else:
            self.web.page().setUrl(QUrl(self.URL_TEACHER))

    def cargar_botones(self):
        if self.clase["ROL"] == "Alumno":

            self.btn_participate.move(60, 460)
            self.btn_participate.setStyleSheet(
                "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
            self.btn_participate.setMinimumSize(190, 30)
            self.btn_participate.setEnabled(False)
            self.btn_participate.clicked.connect(self.click_participate)
        else:
            self.btn_give_participate = QPushButton('Ceder', self)
            self.btn_give_participate.move(60, 460)
            self.btn_give_participate.setStyleSheet(
                "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
            self.btn_give_participate.setMinimumSize(60, 30)
            self.btn_give_participate.setEnabled(False)
            self.btn_give_participate.clicked.connect(self.click_give_participation)

            self.btn_recover= QPushButton('Recuperar', self)
            self.btn_recover.move(160, 460)
            self.btn_recover.setStyleSheet(
                "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
            self.btn_recover.setMinimumSize(100, 30)
            self.btn_recover.setEnabled(False)
            self.btn_recover.clicked.connect(self.click_recover)

    def activar_btn_participacion(self):
        self.btn_participate.setEnabled(True)
        self.btn_participate.clicked.connect(self.click_participate)

    def desactivar_btn_participacion(self):
        self.btn_participate.setEnabled(False)

    def click_participate(self):
        self.transport.open()
        nombre_equipo = socket.gethostname()
        direccion_equipo = socket.gethostbyname(nombre_equipo)
        self.client.pedirParticipacion(self.nombre_usuario,direccion_equipo)
        self.transport.close()

    def click_give_participation(self):
        self.lbl_reques.setVisible(False)
        self.btn_recover.setEnabled(True)
        self.transport.open()
        self.client.otorgarParticipacion(self.ip_solicitante)
        self.lbl_reques.setVisible(False)
        self.transport.close()

    def click_recover(self):
        self.transport.open()
        self.client.recuperarControl(self.ip_solicitante)
        self.transport.close()


    def iniciar_conectados(self):
        self.transport.open()
        nombre_equipo = socket.gethostname()
        direccion_equipo = socket.gethostbyname(nombre_equipo)
        print(direccion_equipo)
        self.rol=1
        if self.clase["ROL"]=="Alumno":
            self.rol=0
        self.client.entrarAula(self.nombre_usuario,direccion_equipo,self.clase["CLASE"],self.rol)
        # client.entrarAula("Susana González", "localhost", "Inteligencia")

        self.transport.close()

    def actualizarListaUsuarios(self, conectados):
        self.list.clear()
        alumnos = conectados
        for alumno in alumnos:
            self.list.addItem(alumno)
            # item = QStandardItem(alumno)
            # self.model.appendRow(item)

        # self.list.setModel(self.model)
        print("Señal recibida")

    def recibirMensaje(self,mensaje):
        self.txt_messages.append(mensaje)
        print("Señal recibida")
        # self.close()
        # windowClasses = WindowClasses

    def click_exit(self,event):
        nombre_equipo = socket.gethostname()
        direccion_equipo = socket.gethostbyname(nombre_equipo)

        self.transport.open()
        self.client.salirAula(self.nombre_usuario,direccion_equipo,self.clase["CLASE"],self.rol)
        self.transport.close()

        self.close()

        # windowClasses=WindowClasses


    def click_send(self):

        self.transport.open()

        self.msg = self.txt_message.toPlainText()
        print(self.msg)
        self.txt_message.setText("")

        self.client.enviarMensaje(self.nombre_usuario+ ": "+self.msg,self.clase["CLASE"])

        self.transport.close()

        print("Salida")


    def ask_participation(self):
        self.lbl_logout = QLabel('Cerrar sesión', self)
        self.lbl_logout.move(830, 40)
        self.lbl_logout.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")

        self.lbl_logout.setMinimumSize(100, 15)
        self.lbl_logout.mousePressEvent = self.click_exit



    def ceder_control_video(self):
        self.web.page().setUrl(QUrl(self.URL_STUDENT))

    def tomar_control_video(self):
        self.web.page().setUrl(QUrl(self.URL_TEACHER))

    def show_reques(self, datos_alumno):
        self.alumno_solicitante = datos_alumno[0]
        self.ip_solicitante = datos_alumno[1]
        self.btn_give_participate.setEnabled(True)
        self.lbl_reques = QLabel('Solicitud del control de video de: '+ datos_alumno[0], self)
        self.lbl_reques.move(60, 110)
        self.lbl_reques.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")

        self.lbl_reques.setMinimumSize(500, 15)
        self.lbl_reques.setVisible(True)



    def permisos(self, url, feature):
        self.web.page().setFeaturePermission(QUrl("http://localhost:5080/demos/simpleBroadcaster.html"),
                                        QWebEnginePage.MediaAudioVideoCapture, QWebEnginePage.PermissionGrantedByUser)
