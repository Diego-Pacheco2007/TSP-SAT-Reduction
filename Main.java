package tsp_sat;

import java.io.File;
import java.util.Scanner;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;

public class Main{

    // Funcion para mapear la variable X_{i,t} a un entero unico para DIMACS
    // i = nodo, t = tiempo, N = total de nodos
    public static int varId(int i, int t, int N) {
        return i * N + t + 1;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java MainTSP <archivo_grafo.txt> <limite_K>");
            return;
        }

        String archivo = args[0];
        int limiteK = Integer.parseInt(args[1]);

        try (Scanner scanner = new Scanner(new File(archivo))) {
            int N = scanner.nextInt();
            int[][] distancias = new int[N][N];

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    distancias[i][j] = scanner.nextInt();
                }
            }

            // Inicializar el SAT-Solver
            ISolver solver = SolverFactory.newDefault();
            int totalVariables = N * N;
            solver.newVar(totalVariables);
            solver.setExpectedNumberOfClauses(10000); 

            // RESTRICCION 1: Visita Unica (Cada nodo se visita exactamente una vez)
            for (int i = 0; i < N; i++) {
                VecInt alMenosUnaVez = new VecInt();
                for (int t = 0; t < N; t++) {
                    alMenosUnaVez.push(varId(i, t, N));
                }
                solver.addClause(alMenosUnaVez); 

                for (int t1 = 0; t1 < N; t1++) {
                    for (int t2 = t1 + 1; t2 < N; t2++) {
                        VecInt aLoMuchoUnaVez = new VecInt();
                        aLoMuchoUnaVez.push(-varId(i, t1, N));
                        aLoMuchoUnaVez.push(-varId(i, t2, N));
                        solver.addClause(aLoMuchoUnaVez);
                    }
                }
            }

            // RESTRICCION 2: Ocupacion Unica (En cada instante t solo hay un nodo)
            for (int t = 0; t < N; t++) {
                VecInt alMenosUnNodo = new VecInt();
                for (int i = 0; i < N; i++) {
                    alMenosUnNodo.push(varId(i, t, N));
                }
                solver.addClause(alMenosUnNodo);

                for (int i1 = 0; i1 < N; i1++) {
                    for (int i2 = i1 + 1; i2 < N; i2++) {
                        VecInt aLoMuchoUnNodo = new VecInt();
                        aLoMuchoUnNodo.push(-varId(i1, t, N));
                        aLoMuchoUnNodo.push(-varId(i2, t, N));
                        solver.addClause(aLoMuchoUnNodo);
                    }
                }
            }


            System.out.println("Iniciando resolucion SAT para N=" + N + " y K=" + limiteK);
            long inicio = System.currentTimeMillis();

            if (solver.isSatisfiable()) {
                System.out.println("Resultado: SATISFIABLE (SAT)");
                int[] modelo = solver.findModel();
                System.out.print("Ruta encontrada: ");
                
                // Reconstruir la ruta a partir de las variables positivas
                int[] ruta = new int[N];
                for (int val : modelo) {
                    if (val > 0) {
                        int index = val - 1;
                        int nodo = index / N;
                        int tiempo = index % N;
                        ruta[tiempo] = nodo + 1;
                    }
                }
                for (int r : ruta) {
                    System.out.print(r + " -> ");
                }
                System.out.println(ruta[0]); 
            } else {
                System.out.println("Resultado: UNSATISFIABLE (UNSAT)");
                System.out.println("NO existe ninguna ruta con costo menor o igual a K");
            }

            long fin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecucion: " + (fin - inicio) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}