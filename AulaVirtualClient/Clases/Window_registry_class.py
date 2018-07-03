import sys
import glob

sys.path.append('gen-py')

import json
from aulavirtual import servicios
from PyQt5.QtGui import QBrush
from PyQt5.QtGui import QPalette
from PyQt5.QtGui import QPixmap
from PyQt5.QtGui import QColor
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
# from Clases.Window_virtual_classroom import WindowClassroom

from PyQt5 import QtGui


class WindowRegistryClass(QMainWindow):

    def __init__(self,client,transport):
        super().__init__()
        self.client = client
        self.transport = transport
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
        self.lbl_name = QLabel("Experiencia educativa", self)
        self.lbl_name.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_name.move(50, 120)
        self.lbl_name.setMinimumSize(180, 25)

        # Text field name
        self.txt_name = QLineEdit(self)
        self.txt_name.move(50, 150)
        self.txt_name.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_name.setMinimumSize(300, 25)

        # Label nrc
        self.lbl_nrc = QLabel("NRC", self)
        self.lbl_nrc.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_nrc.setMinimumSize(200, 200)
        self.lbl_nrc.move(50, 100)

        # Text field nrc
        self.txt_nrc = QLineEdit(self)
        self.txt_nrc.move(50, 215)
        self.txt_nrc.setStyleSheet(
            "font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_nrc.setMinimumSize(300, 25)

        # Label maestro
        self.lbl_periodo = QLabel("Periodo", self)
        self.lbl_periodo.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_periodo.setMinimumSize(200, 200)
        self.lbl_periodo.move(50, 165)

        # ComboBox maestro
        self.cb_periodo = QComboBox(self)
        self.cb_periodo.move(50, 280)
        self.cb_periodo.setMinimumSize(300, 25)

        # Label maestro
        self.lbl_maestro = QLabel("Maestro", self)
        self.lbl_maestro.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_maestro.setMinimumSize(200, 200)
        self.lbl_maestro.move(50, 220)

        # ComboBox maestro
        self.cb_maestro = QComboBox(self)
        self.cb_maestro.move(50, 335)
        self.cb_maestro.setMinimumSize(300, 25)



        # Button to register
        self.btn_registry = QPushButton('Registrar clase', self)
        # self.btn_login.resize()
        self.btn_registry.move(125, 400)
        self.btn_registry.setMinimumSize(150, 30)
        self.btn_registry.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        # self.btn_registry.clicked.connect(self.registry_clicked)
        # # cell.clicked.connect(self.buttonClicked)


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

        # Set baackground
        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()
        self.obtener_periodos()

    def obtener_campos(self):
        self.nombre = self.txt_name.text()
        self.nrc = self.txt_nrc.text()

    def validar_campos(self):
        return self.txt_name.text() == "" or self.txt_name.text() == None or self.txt_nrc.text() == "" or self.txt_nrc.text() == None

    #
    # def registry_clicked(self):


    def obtener_periodos(self):
        self.periodos = self.client.obtenerPeriodos()
        for periodo in self.periodos:
            p = json.loads(periodo)
            self.cb_periodo.addItem(list(p.values())[0])



if __name__ == '__main__':
    app = QApplication(sys.argv)
    windowRegistryClass = WindowRegistryClass()
    sys.exit(app.exec_())