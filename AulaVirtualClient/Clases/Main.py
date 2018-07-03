import sys
import glob
sys.path.append('gen-py')
# sys.path.insert(0, glob.glob('../../lib/py/build/lib*')[0])
from PyQt5.QtWidgets import QApplication
from Window_virtual_classroom import WindowClassroom



from Window_login import WindowLogin
from Prueba import Prueba
from Implementacion import Implementacion
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from aulavirtual import servicios
from Server import MiHilo



if __name__ == '__main__':
    app = QApplication(sys.argv)

    windowLogin = WindowLogin()
    sys.exit(app.exec_())


