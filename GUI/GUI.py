from tkinter import *
from tkinter import filedialog
import os
from Anim import func


def Main():
    if input1 != '' and input2 != '':
        if os.path.exists('Map.txt'):
            os.remove('Map.txt')
        if os.path.exists('Result.txt'):
            os.remove('Result.txt')
        command = 'java -jar Core.jar ' + subtractdir(e1.get()) + ' ' + subtractdir(e2.get())
        os.system(command)
        if os.path.exists('Map.txt') and os.path.exists('Result.txt'):
            func('Map.txt', 'Result.txt', scale.get(), 1)


input1 = ''


def in1():
    global input1
    input1 = filedialog.askopenfilename()
    e1.delete(0, len(e1.get()))
    e1.insert(0, input1)


input2 = ''


def in2():
    global input2
    input2 = filedialog.askopenfilename()
    e2.delete(0, len(e2.get()))
    e2.insert(0, input2)


root = Tk()
root.title("APA")
root.geometry("400x300")

e1 = Entry(root, width=57)
e1.place(x=25, y=20)
myButton = Button(root, text='Select map file', command=in1, width=48)
myButton.place(x=25, y=50)

e2 = Entry(root, width=57)
e2.place(x=25, y=90)
myButton = Button(root, text='Select patient file', command=in2, width=48)
myButton.place(x=25, y=120)

frames = IntVar()


def subtractdir(path):
    curr_dir = os.getcwd().replace('\\', '/') + '/'
    if curr_dir in path:
        return path.replace(curr_dir, '')
    else:
        return path


taki = Label(root, text='Animation frames per cycle:', width=53)
taki.place(x=12, y=170)
scale = Scale(root, variable=frames, from_=1, to=100, length=340, orient=HORIZONTAL)
scale.place(x=25, y=185)

myButton = Button(root, text='Begin', command=Main, width=48)
myButton.place(x=25, y=250)

root.mainloop()
