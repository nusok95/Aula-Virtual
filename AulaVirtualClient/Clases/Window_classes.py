import sys
import glob

sys.path.append('gen-py')

import json
from aulavirtual import servicios
from PyQt5.QtGui import QBrush
from PyQt5.QtGui import QPalette
from PyQt5.QtGui import QPixmap
from PyQt5.QtGui import QColor
from PyQt5.QtWidgets import QListWidget
from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QDesktopWidget
from PyQt5.QtWidgets import QLabel
from PyQt5.QtWidgets import QMainWindow, QMdiSubWindow
from PyQt5.QtWidgets import QMessageBox
from PyQt5.QtWidgets import QTextEdit
from PyQt5.QtWidgets import QLineEdit
from PyQt5.QtWidgets import QPushButton
from PyQt5.QtWidgets import QComboBox
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from Window_virtual_classroom import WindowClassroom
from Window_inscribe_class import WindowInscribeClass

from PyQt5 import QtGui


class WindowClasses(QMainWindow):

    def __init__(self,client,transport,usuario,nombre_usuario):
        super().__init__()
        self.client=client
        self.transport = transport
        self.usuario = usuario
        # self.hMiHilo=hilo
        self.nombre_usuario = nombre_usuario
        self.initUI()
        self.show()

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
        self.setFixedSize(400, 500)
        self.setWindowTitle('Registro de Experiencia educativa')
        # self.setWindowIcon(QtGui.QIcon('Clases/images/b6.ico'))


        # Label name
        self.lbl_name = QLabel("Clases disponibles", self)
        self.lbl_name.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_name.move(50, 120)
        self.lbl_name.setMinimumSize(180, 25)

        self.list = QListWidget(self)
        self.list.setMinimumSize(300, 230)
        self.list.move(50, 150)



        # Button to register
        self.btn_enter = QPushButton('Entrar', self)
        # self.btn_login.resize()
        self.btn_enter.move(125, 400)
        self.btn_enter.setMinimumSize(150, 30)
        self.btn_enter.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_enter.clicked.connect(self.enter_clicked)
        # cell.clicked.connect(self.buttonClicked)

        # Button to register
        self.btn_registry = QPushButton('Inscribir clase', self)
        # self.btn_login.resize()
        self.btn_registry.move(125, 450)
        self.btn_registry.setMinimumSize(150, 30)
        self.btn_registry.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_registry.clicked.connect(self.inscribe_clicked)
        # cell.clicked.connect(self.buttonClicked)


        # Label empty fields
        self.lbl_empty_fields = QLabel("Hay campos vacios", self)
        self.lbl_empty_fields.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_empty_fields.move(50, 570)
        self.lbl_empty_fields.setMinimumSize(300, 30)
        self.lbl_empty_fields.setVisible(False)

        # Label invalid fields
        self.lbl_invalid_password = QLabel("La contraseña no coincide", self)
        self.lbl_invalid_password.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_invalid_password.move(50, 570)
        self.lbl_invalid_password.setMinimumSize(300, 30)
        self.lbl_invalid_password.setVisible(False)

        # Log out button
        self.lbl_exit = QLabel('Cerrar sesión', self)
        self.lbl_exit.move(270, 40)
        self.lbl_exit.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")
        self.lbl_exit.setMinimumSize(100, 15)
        self.lbl_exit.mousePressEvent = self.exit_clicked

        # Set baackground
        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()
        self.load_list()

    def obtener_campos(self):
        self.nombre = self.txt_name.text()
        self.nrc = self.txt_nrc.text()

    def validar_campos(self):
        return self.txt_name.text() == "" or self.txt_name.text() == None or self.txt_nrc.text() == "" or self.txt_nrc.text() == None

    def load_list(self):
        self.transport.open()
        self.clases = self.client.obtenerClasesUsuario(self.usuario)
        self.transport.close()
        for clase in self.clases:
            print(clase)
            clase_dic = json.loads(clase)
            self.list.addItem(clase_dic["CLASE"])

    def get_class(self):
        for clase in self.clases:
            clase_dic = json.loads(clase)
            if self.nombre_clase == clase_dic["CLASE"]:
                self.clase = clase_dic
                return


    def enter_clicked(self):
        self.nombre_clase = self.list.currentItem().text()

        self.get_class()
        self.windowClassrom = WindowClassroom(self.client,self.transport,self.clase,self.usuario,self.nombre_usuario)
        self.windowClassrom.show()


    def inscribe_clicked(self):
        print("INSCRIBIR")
        self.windowInscribeClass = WindowInscribeClass(self.usuario,self.client,self.transport)


    def exit_clicked(self, event):
        self.close()

# if __name__ == '__main__':
#     app = QApplication(sys.argv)
#     windowRegistryClass = WindowClasses()
#     sys .exit(app.exec_())