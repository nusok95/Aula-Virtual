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
from Window_registry_class import WindowRegistryClass
from PyQt5 import QtGui


class WindowAdmin(QMainWindow):
    def __init__(self,client, transport):
        super().__init__()
        self.client=client
        self.transport=transport
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

        self.btn_registry_class = QPushButton('Registrar clase', self)
        # self.btn_login.resize()
        self.btn_registry_class.move(125, 200)
        self.btn_registry_class.setMinimumSize(160, 40)
        self.btn_registry_class.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_registry_class.clicked.connect(self.registry_class_clicked)


        self.btn_registry_teacher = QPushButton('Registrar maestro', self)
        # self.btn_login.resize()
        self.btn_registry_teacher.move(125, 300)
        self.btn_registry_teacher.setMinimumSize(160, 40)
        self.btn_registry_teacher.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        # self.btn_registry.clicked.connect(self.registry_clicked)

        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor("#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()

    def registry_class_clicked(self):
        self.windowRegistryClass = WindowRegistryClass(self.client, self.transport)
        self.windowRegistryClass.show()




if __name__ == '__main__':
    app = QApplication(sys.argv)
    windowAdmin = WindowAdmin()
    sys.exit(app.exec_())