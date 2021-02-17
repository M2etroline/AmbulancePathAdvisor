import random

def randmap(x,y,s,o,d):
    file = open('dane1.txt','w')
    file.write('#Szpitale\n')
    for Szpital in range(s):
        file.write(str(Szpital)+' | '+str(Szpital)+' | '+str(random.randint(x,y))+' | '+str(random.randint(x,y))+' | 1000 | 1\n')
    file.write('#Obiekty\n')
    for Obiekt in range(o):
        file.write(str(Obiekt)+' | '+str(Obiekt)+' | '+str(random.randint(x,y))+' | '+str(random.randint(x,y))+'\n')
    file.write('#Drogi\n')
    wszytkiedrogi=[]
    for i in range(s):
        for x in range(s):
            wszytkiedrogi.append([i,x])
    for i in wszytkiedrogi:
        if i[0] == i[1]:
            wszytkiedrogi.remove(i)
    for Droga in range(d):
        wybrana_droga=wszytkiedrogi[random.randint(0,len(wszytkiedrogi)-1)]
        wszytkiedrogi.remove(wybrana_droga)
        file.write(str(Droga)+' | '+str(wybrana_droga[0])+' | '+str(wybrana_droga[1])+' | '+str(random.randint(1,15)*100)+'\n')

def randppl(x,y,p):
    file = open('dane2.txt','w')
    file.write('#Pacjenci\n')
    for Pacjent in range(p):
        file.write(str(Pacjent)+' | '+str(random.randint(x,y))+' | '+str(random.randint(x,y))+'\n')

randmap(-1000,1000,20,100,100)
randppl(-10,150,500)
