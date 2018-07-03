import sys
import glob



sys.path.append('gen-py')
import json
from aulavirtual import servicios
from PyQt5.QtGui import QBrush
from PyQt5.QtGui import QPalette
from PyQt5.QtGui import QPixmap
from PyQt5.QtGui import QColor
from PyQt5.QtWidgets import QDesktopWidget
from PyQt5.QtWidgets import QLabel
from PyQt5.QtWidgets import QMainWindow, QMdiSubWindow
from PyQt5.QtWidgets import QLineEdit
from PyQt5.QtWidgets import QPushButton
from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QFrame
from PyQt5.QtWidgets import QGridLayout
from PyQt5.QtGui import QPainter
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from Window_registry_student import WindowRegistryStudent
from Window_admin import WindowAdmin
from Window_classes import WindowClasses
from Server import MiHilo

from PyQt5.QtWidgets import QComboBox
from PyQt5 import QtGui




class WindowLogin(QMainWindow):
    def __init__(self):
        super().__init__()
        self.log = {}
        # self.hMiHilo = 2
        # self.hMiHilo.start()
        self.initUI()
        # self.show()

    def initUI(self):
        # Add core elements for the window
        self.lblDesign = QLabel("", self)
        self.lblDesign.setStyleSheet(
            "background-color: #019A74; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.lblDesign.setMinimumSize(400, 70)

        self.lbl_tittle1 = QLabel("Aula ", self)
        self.lbl_tittle1.setStyleSheet(
            "font-weight: b  old; color: white; font-family: century gothic; font-size: 32px")
        self.lbl_tittle1.move(100, 80)
        self.img_logo = QLabel(self)
        pixmap = QPixmap("Images/mini_Teacher3.png")
        self.img_logo.setPixmap(pixmap)
        self.img_logo.move(20, -12)
        self.img_logo.setMinimumSize(100, 100)

        self.lbl_tittle2 = QLabel("Virtual", self)
        self.lbl_tittle2.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 32px")
        self.lbl_tittle2.setMinimumSize(110, 30)
        self.lbl_tittle2.move(192, 80)

        # This window
        self.setFixedSize(400, 650)
        # self.center()
        self.setWindowTitle('Inicio de sesión')
        # self.setWindowIcon(QtGui.QIcon('Clases/images/b6.ico'))

        # Label username
        self.lbl_username = QLabel("Matricula", self)
        self.lbl_username.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 20px")
        self.lbl_username.move(50, 180)

        # Text field USERNAME
        self.txt_username = QLineEdit(self)
        self.txt_username.move(50, 220)
        self.txt_username.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 16px")
        self.txt_username.setMinimumSize(300, 35)

        # Label password
        self.lbl_password = QLabel("Contraseña", self)
        self.lbl_password.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 20px")
        self.lbl_password.move(50, 280)
        self.lbl_password.setMinimumSize(300, 35)

        # Text field Password
        self.txt_password = QLineEdit(self)
        self.txt_password.setEchoMode(QLineEdit.Password)
        self.txt_password.move(50, 320)
        self.txt_password.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 16px")
        self.txt_password.setMinimumSize(300, 35)

        # Label empty fields
        self.lbl_empty_fields = QLabel("*Campos vacios", self)
        self.lbl_empty_fields.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 16px")
        self.lbl_empty_fields.move(250, 150)
        self.lbl_empty_fields.setMinimumSize(300, 30)
        self.lbl_empty_fields.setVisible(False)

        # Button to login
        self.btn_login = QPushButton('Iniciar sesión', self)
        # self.btn_login.resize()
        self.btn_login.move(155, 400)
        self.btn_login.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_login.clicked.connect(self.iniciar_sesion_clicked)
        # cell.clicked.connect(self.buttonClicked)

        # Label password
        self.lbl_question = QLabel("¿No tienes una cuenta?", self)
        self.lbl_question.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_question.move(45, 450)
        self.lbl_question.setMinimumSize(300, 35)

        # Label invalid username or password
        self.lbl_invalid_account = QLabel("*Ha ingresado un nombre de usuario o la\ncontraseña incorrectos.", self)
        self.lbl_invalid_account.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_invalid_account.move(50, 340)
        self.lbl_invalid_account.setMinimumSize(300, 70)
        self.lbl_invalid_account.setVisible(False)

        self.lbl_account = QLabel("Crea una", self)
        self.lbl_account.setStyleSheet("font-weight: bold; color: ORANGE; font-family: century gothic; font-size: 15px")
        self.lbl_account.move(255, 450)
        self.lbl_account.setMinimumSize(300, 35)
        self.lbl_account.mousePressEvent = self.crear_cuenta_clicked

        # Label empty fields
        self.lbl_empty_fields = QLabel("Hay campos vacios", self)
        self.lbl_empty_fields.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_empty_fields.move(50, 570)
        self.lbl_empty_fields.setMinimumSize(300, 30)
        self.lbl_empty_fields.setVisible(False)

        self.lbl_invalid_login = QLabel("Verifique su matricula y contraseña", self)
        self.lbl_invalid_login.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_invalid_login.move(50, 570)
        self.lbl_invalid_login.setMinimumSize(300, 30)
        self.lbl_invalid_login.setVisible(False)

        # Set baackground
        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()

    def validar_campos(self):
        username = self.txt_username.text()
        password = self.txt_password.text()
        return not username == "" and not username.isspace() and not password == "" and not password.isspace()

    def obtener_campos(self):
        self.usuario = self.txt_username.text()
        self.password = self.txt_password.text()

    def iniciar_sesion(self):
        if (self.validar_campos()):
            self.obtener_campos()
            transport = TSocket.TSocket('localhost', 9090)

            # Buffering is critical. Raw sockets are very slow
            transport = TTransport.TBufferedTransport(transport)

            # Wrap in a protocol
            protocol = TBinaryProtocol.TBinaryProtocol(transport)

            # Create a client to use the protocol encoder
            client = servicios.Client(protocol)

            self.obtener_campos()

            # Connect!
            transport.open()

            response_json = client.iniciarSesion(self.usuario, self.password)


            self.response = json.loads(response_json)

            transport.close()

            if self.response["success"] != True:
                self.lbl_invalid_login.setVisible(True)
            else:
                if self.response["tipo_usuario"] == 1:
                    self.windowAdmin = WindowAdmin(client,transport)
                    self.windowAdmin.show()
                    self.close()
                else:
                    self.windowClasses = WindowClasses(client,transport,self.usuario,self.response["nombre"])
                    self.windowClasses.show()

        else:
            self.lbl_empty_fields.setVisible(True)

    def iniciar_sesion_clicked(self):

        self.iniciar_sesion()






    def crear_cuenta_clicked(self, event):
        # self.close()
        self.windowRegistryStudent = WindowRegistryStudent()

        # self.windowRegistry.show()
