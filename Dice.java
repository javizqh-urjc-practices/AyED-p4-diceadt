package ayedd.e2;
// Es un programa que recibe una array con tiradas y sus valores, los organiza y saca una array con las 10 mejores

import edu.princeton.cs.algs4.*;

public class Dice {

    public static String[] topJugadores= new String[10];
    public static int[] topTiradas= new int[10];

    public static String[] encontrarJugadores(String[] arr){
        //Obtiene loos nombres de los jugadores
        String[] jugadores= new String[arr.length/3];
        for (int i=0; i<arr.length;i+=3){ 
            jugadores[i/3]=arr[i];         
        }
        return jugadores;
    }

    public static int[] encontrarPuntuaciones(String[] tiradasArr, String[] valores){
        //Obtiene los valores de las diferentes tiradas
        int indexValores=0;
        int[] juegoValoresInt = new int[valores.length*3];
        String[] juegoValoresSeparados = new String[3];

        for (int i=0; i<valores.length;i++){
            juegoValoresSeparados = valores[i].split(" ");
            for (int j=0;j<juegoValoresSeparados.length;j++){
                juegoValoresInt[indexValores]=Integer.parseInt(juegoValoresSeparados[j]);
                indexValores++;
            }
        }
        //Obtiene las tiradas realizadas
        int[] tiradas = new int[(2*tiradasArr.length)/3];
        for (int i=0; i<tiradasArr.length;i+=3){               
            tiradas[(i*2)/3]=Integer.parseInt(tiradasArr[i+1]);
            tiradas[(3+i*2)/3]=Integer.parseInt(tiradasArr[i+2]);
        }
        //Calcula las puntuaciones de las tiradas
        int[] puntuaciones=new int[valores.length];
        for (int j=1;j<tiradas.length;j+=2){
            for (int i=0;i<juegoValoresInt.length;i+=3){
                if (tiradas[j-1]==juegoValoresInt[i]){
                    if  (tiradas[j]==juegoValoresInt[i+1]){
                        puntuaciones[(j-1)/2]=juegoValoresInt[i+2];
                    }
                }
            }
        }
        return puntuaciones;
    }

    public static void jugadoresRepetidos (String nombre,int puntuacion){
        //Como hay un jugador con el mismo nombre comprueba cual es el que tiene mayor puntuacion
        for (int i=0;i<topJugadores.length;i++){
            if (topJugadores[i]!=null){
                if (nombre.equals(topJugadores[i])){
                    if (puntuacion>topTiradas[i]){
                        //Si tiene mayor puntuacion lo remplaza
                        topTiradas[i]=puntuacion;
                    } 
                }
            }
        }   
    }   

    public static boolean jugadorNoEstaRepetido (String nombre){
        //Devuelve True si no esta el nombre repetido y false si lo esta
        for (int i=0;i<topJugadores.length;i++){
            if (topJugadores[i]!=null){
                if (nombre.equals(topJugadores[i])){
                    return false;
                } 
            }   
        }
        return true;
    }

    public static int[] encontrarMenor(){
        //Encuentra la menor puntuacion dentro del top
        int[] menor = {topTiradas[0],0};
        for (int i=0;i<topTiradas.length;i++){
            if (topTiradas[i]<menor[0]){
                menor[0] = topTiradas[i];
                menor[1] = i;
            }
        }
        return menor;
    }
    
    public static void sustituirJugador(String nombre,int puntuacion){
        //Comprueba si el nuevo jugador posee una mayor puntuacion que alguno del top y lo sustituye
        int[] menor = encontrarMenor();
        for (int i=0;i<topJugadores.length;i++){
            if ((puntuacion>menor[0])&&(menor[1]==i)){
                //Si tiene una mayor puntuacion
                topJugadores[i]=nombre;
                topTiradas[i]=puntuacion;
            }
        }
    }
    public static void resetTop (){
        String[] originalTopJugadores = new String[10];
        topJugadores = originalTopJugadores;
        int[] originalTopTiradas = new int[10];
        topTiradas = originalTopTiradas;
    }

    public static String[] top(String jugadaFname, String tiradasFname) {
        //Import ficheros
        String[] juegoTiradas = new In(tiradasFname).readAllLines();
        String[] juegoValores = new In(jugadaFname).readAllLines();
        //Llamo a las funciones
        String[] jugadores = encontrarJugadores(juegoTiradas);
        int[] tiradasValores = encontrarPuntuaciones(juegoTiradas, juegoValores);
        //Devuelve los nombres de los jugadores que han sacado mayor puntuacion
        int jugadoresEnElTop =0;
        for (int i=0;i<jugadores.length;i++){  
            if (jugadorNoEstaRepetido(jugadores[i])){
                if (jugadoresEnElTop<10){
                    topJugadores[jugadoresEnElTop]=jugadores[i];
                    topTiradas[jugadoresEnElTop] = tiradasValores[i];
                    jugadoresEnElTop++;
                } else {
                    sustituirJugador(jugadores[i], tiradasValores[i]);
                }
            } else {
                jugadoresRepetidos(jugadores[i], tiradasValores[i]);
            }
        }
        String[] topFinal = topJugadores;
        resetTop();
        return topFinal;
    }

    public static void main(String[] args) {
        if (args.length==0) {
            // Si no tiene argumento devuelve error
            System.err.println("usage: dice tiradas.txt juego.txt");
            System.exit(1);
        } else {
            //Output
            String [] finalTop =  top(args[1], args[0]);
            for (int i=0; i<10;i++){
                if (finalTop[i]!=null){
                    StdOut.println(finalTop[i]);
                }      
            }    
        }
          
    }
}
