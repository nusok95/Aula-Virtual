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
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from Window_virtual_classroom import WindowClassroom


from PyQt5 import QtGui

class WindowRegistryStudent(QMainWindow):
    def __init__(self):
        super().__init__()
        self.TIPO_USUARIO = 3;
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
        self.setFixedSize(400, 650)
        self.setWindowTitle('Registro de usuario')
        #self.setWindowIcon(QtGui.QIcon('Clases/images/b6.ico'))

        # Label name
        self.lbl_name = QLabel("Nombre",self)
        self.lbl_name.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_name.move(50, 120)

         # Text field name
        self.txt_name = QLineEdit(self)
        self.txt_name.move(50,150)
        self.txt_name.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_name.setMinimumSize(300, 25)

        #Label last_name
        self.lbl_last_name = QLabel("Apellido paterno",self)
        self.lbl_last_name.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_last_name.setMinimumSize(200,200)
        self.lbl_last_name.move(50,100)

        #Text field last_name
        self.txt_last_name = QLineEdit(self)
        self.txt_last_name.move(50,215)
        self.txt_last_name.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_last_name.setMinimumSize(300, 25)

        #Label last_name_m
        self.lbl_last_name_m = QLabel("Apellido materno",self)
        self.lbl_last_name_m.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_last_name_m.setMinimumSize(200,200)
        self.lbl_last_name_m.move(50,165)

        #Text field last_name_m
        self.txt_last_name_m = QLineEdit(self)
        self.txt_last_name_m.move(50,280)
        self.txt_last_name_m.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_last_name_m.setMinimumSize(300, 25)

        #Label matricula
        self.lbl_matricula = QLabel("Matrícula",self)
        self.lbl_matricula.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_matricula.move(50, 315)

        #Text field matricula
        self.txt_matricula = QLineEdit(self)
        self.txt_matricula.move(50,345)
        self.txt_matricula.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_matricula.setMinimumSize(300, 25)

        #Label email
        self.lbl_email = QLabel("Correo",self)
        self.lbl_email.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_email.move(50, 380)

        #Text field email
        self.txt_email = QLineEdit(self)
        self.txt_email.move(50,410)
        self.txt_email.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15px")
        self.txt_email.setMinimumSize(300, 25)

         # Label password
        self.lbl_password = QLabel("Contraseña", self)
        self.lbl_password.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_password.move(50, 440)
        self.lbl_password.setMinimumSize(300, 35)

        # Text field Password
        self.txt_password = QLineEdit(self)
        self.txt_password.setEchoMode(QLineEdit.Password)
        self.txt_password.move(50,475)
        self.txt_password.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15")
        self.txt_password.setMinimumSize(300, 25)

        # Label repeat_password
        self.lbl_repeat_password = QLabel("Repetir contraseña", self)
        self.lbl_repeat_password.setStyleSheet("font-weight: bold; color: white; font-family: century gothic; font-size: 15px")
        self.lbl_repeat_password.move(50, 505)
        self.lbl_repeat_password.setMinimumSize(300, 35)

        # Text field repeat_assword
        self.txt_repeat_password = QLineEdit(self)
        self.txt_repeat_password.setEchoMode(QLineEdit.Password)
        self.txt_repeat_password.move(50,540)
        self.txt_repeat_password.setStyleSheet("font-weight: bold; color: black; font-family: century gothic; font-size: 15")
        self.txt_repeat_password.setMinimumSize(300, 25)

        # Button to register
        self.btn_registry = QPushButton('Registrarme', self)
       # self.btn_login.resize()
        self.btn_registry.move(155, 600)
        self.btn_registry.setStyleSheet(
            "background-color: #08AE9E; font-weight: bold; color: White; font-family: century gothic; font-size: 16px")
        self.btn_registry.clicked.connect(self.registry_clicked)
                 #cell.clicked.connect(self.buttonClicked)

        # Label empty fields
        self.lbl_succes = QLabel("Registro realizado correctamente", self)
        self.lbl_succes.setStyleSheet(
            "font-weight: bold; color: orange; font-family: century gothic; font-size: 14px")
        self.lbl_succes.move(50, 570)
        self.lbl_succes.setMinimumSize(300, 30)
        self.lbl_succes.setVisible(False)

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
        self.lbl_exit = QLabel('Regresar', self)
        self.lbl_exit.move(310, 40)
        self.lbl_exit.setStyleSheet(
            "font-weight: bold; color: white; font-family: century gothic; font-size: 14px")
        self.lbl_exit.setMinimumSize(100, 15)
        self.lbl_exit.mousePressEvent = self.exit_clicked


       
        # Set baackground
        palette = QPalette()
        palette.setBrush(QPalette.Background, QBrush(QColor( "#1B528A")))
        # 2F4D6B"
        self.setPalette(palette)
        self.show()

    def obtener_campos(self):
        self.nombre = self.txt_name.text()
        self.apellido_paterno = self.txt_last_name.text()
        self.apellido_materno = self.txt_last_name_m.text()
        self.matricula = self.txt_matricula.text()
        self.correo = self.txt_email.text()
        self.password = self.txt_password.text()
        self.password_repeat = self.txt_repeat_password.text()


    def validar_campos(self):
        nombre = self.txt_name.text()
        apellido_paterno = self.txt_last_name.text()
        apellido_materno = self.txt_last_name_m.text()
        matricula = self.txt_matricula.text()
        correo = self.txt_email.text()
        password = self.txt_password.text()
        password_repeat = self.txt_repeat_password.text()
        return not nombre=="" and not nombre.isspace() and not apellido_materno=="" and not apellido_materno.isspace() \
               and not apellido_paterno=="" and apellido_paterno.isspace() and not matricula=="" and not matricula.isspace() \
               and not correo.text()=="" and not correo.isspace() and not password=="" and not password.isspace() \
               and not password_repeat=="" and not password_repeat.isspace()

    def validar_password(self):
        return self.txt_password.text() == self.txt_repeat_password.text()

    def realizar_registro(self):
        if(not self.validar_campos()):
            if(self.validar_password()):
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
                response = client.registrarse(self.matricula, self.nombre, self.apellido_paterno, self.apellido_materno,
                                          self.correo, self.password,self.TIPO_USUARIO)
                status = json.loads(response)
                status = status['success']
                print(status)
                if status:
                    self.lbl_succes.setVisible(True)
                    # self.close()
                    # self.WindowClassroom = WindowClassroom()
                else:
                    self.lbl_invalid_password.setVisible(True)

                transport.close()
            else:
                self.lbl_invalid_password.setVisible(True)
        else:
            self.lbl_empty_fields.setVisible(True)

    def registry_clicked(self):
        self.lbl_empty_fields.setVisible(False)
        self.lbl_invalid_password.setVisible(False)
        self.realizar_registro()

    def exit_clicked(self,event):
        self.hide()


# if __name__ == '__main__':
#     app = QApplication(sys.argv)
#     windowRegistry = WindowRegistryStudent()
#     sys.exit(app.exec_())
