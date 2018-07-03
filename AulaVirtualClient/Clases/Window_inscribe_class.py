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


class WindowInscribeClass(QMainWindow):

    def __init__(self,usuario,client,transport):
        super().__init__()
        self.usuario = usuario
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
        self.lbl_nrc = QLabel("NRC", self)
        self.lbl_nrc.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_nrc.move(50, 120)
        self.lbl_nrc.setMinimumSize(180, 25)


        # Text field name
        self.cb_nrc = QComboBox(self)
        self.cb_nrc.move(50, 150)
        self.cb_nrc.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.cb_nrc.setMinimumSize(300, 25)



        # Label experiencia educativa
        self.lbl_ee = QLabel("Experiencia educativa", self)
        self.lbl_ee.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_ee.setMinimumSize(200, 200)
        self.lbl_ee.move(50, 150)

        # Text field experiencia educativa
        self.txt_ee = QLineEdit(self)
        self.txt_ee.move(50, 265)
        self.txt_ee.setStyleSheet(
            "font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_ee.setMinimumSize(300, 25)
        self.txt_ee.setEnabled(False)

        # Label maestro
        self.lbl_maestro = QLabel("Maestro", self)
        self.lbl_maestro.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_maestro.setMinimumSize(200, 200)
        self.lbl_maestro.move(50, 215)

        # Text maestro
        self.txt_maestro = QLineEdit(self)
        self.txt_maestro.move(50, 330)
        self.txt_maestro.setStyleSheet(
            "font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_maestro.setMinimumSize(300, 25)
        self.txt_maestro.setEnabled(False)
        # Button to register
        self.btn_search= QPushButton('Buscar', self)
        # self.btn_login.resize()
        self.btn_search.move(125, 190)
        self.btn_search.setMinimumSize(150, 30)
        self.btn_search.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_search.clicked.connect(self.click_search)

        # Button to register
        self.btn_registry = QPushButton('Inscribir clase', self)
        # self.btn_login.resize()
        self.btn_registry.move(125, 400)
        self.btn_registry.setMinimumSize(150, 30)
        self.btn_registry.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_registry.clicked.connect(self.click_registry)
        # cell.clicked.connect(self.buttonClicked)


        # Label empty fields
        self.lbl_succes = QLabel("Clase inscrita correctamente", self)
        self.lbl_succes.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_succes.move(50, 450)
        self.lbl_succes.setMinimumSize(300, 30)
        self.lbl_succes.setVisible(False)

        # Label invalid fields
        self.lbl_invalid = QLabel("Clase ya inscrita", self)
        self.lbl_invalid.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_invalid.move(50, 450)
        self.lbl_invalid.setMinimumSize(300, 30)
        self.lbl_invalid.setVisible(False)

        # Log out button
        self.lbl_exit = QLabel('Regresar', self)
        self.lbl_exit.move(270, 40)
        self.lbl_exit.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")
        self.lbl_exit.setMinimumSize(100, 15)
        self.lbl_exit.mousePressEvent = self.click_exit

        # Set baackground
        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()
        self.inicialize_nrc()

    def inicialize_nrc(self):
        self.get_classes()
        for clase in self.all_classes:
            ban=1
            self.classes_dic = json.loads(clase)
            for clase_usuario in self.user_classes:
                self.clase_user_dic = json.loads(clase_usuario)
                if self.clase_user_dic["NRC"]==self.classes_dic["NRC"]:
                    ban=0;
            if ban==1:
                self.cb_nrc.addItem(self.classes_dic["NRC"])

    def get_classes(self):
        self.transport.open()
        self.all_classes = self.client.obtenerClases()
        self.user_classes = self.client.obtenerClasesUsuario(self.usuario)
        self.transport.close()

    def click_search(self):
        self.lbl_succes.setVisible(False)
        self.lbl_invalid.setVisible(False)
        nrc_selected = self.cb_nrc.currentText()
        print("hola")
        print(nrc_selected)
        for clase in self.all_classes:
            clase_dic = json.loads(clase)
            if nrc_selected == clase_dic["NRC"]:
                self.txt_ee.setText(clase_dic["NOMBRE"])
                self.txt_maestro.setText(clase_dic["MAESTRO"])

    def click_exit(self, event):
        self.close()

    def click_registry(self):
        self.lbl_succes.setVisible(False)
        self.lbl_invalid.setVisible(False)
        ban=1
        self.get_classes()
        nrc_selected = self.cb_nrc.currentText()
        self.txt_maestro.setText("")
        self.txt_ee.setText("")
        for clase_usuario in self.user_classes:
            self.clase_user_dic = json.loads(clase_usuario)
            if self.clase_user_dic["NRC"] == nrc_selected:
                ban = 0

        if ban == 1:
            self.transport.open()
            self.client.inscribirClase(self.usuario,nrc_selected,"Alumno")
            self.transport.close()
            self.lbl_succes.setVisible(True)
        else:
            self.lbl_invalid.setVisible(True)




# if __name__ == '__main__':
#     app = QApplication(sys.argv)
#     windowInscribeClass = WindowInscribeClass()
#     sys.exit(app.exec_())