import java.util.Random;

class calcul_pi_cercle { 
    double[] resultats = new double[4];

    class MyThread extends Thread { 
        private int my_num;

        public void run (){
            System.out.printf("Thread numéro %d lancé%n", my_num); 
            int nb_vrai = 0;
            int N = 1000000;
            Random random = new Random();

            for (int i = 0; i < N; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();

                if (x * x + y * y <= 1) {
                    nb_vrai += 1;
                }
            }

            double proba_locale = (double) nb_vrai / N;
            synchronized (this){
                resultats[my_num - 1] = proba_locale;
            }
        }

        public MyThread(int my_num) { // Constructeur
            this.my_num = my_num;
        } 
    }

    public static void main(String[] args) { 
        calcul_pi_cercle instance = new calcul_pi_cercle(); // Créez une instance de calcul_pi
		double proba = 0;
        MyThread th1 = instance.new MyThread(1); // Utilisez l'instance pour créer les threads
        MyThread th2 = instance.new MyThread(2); 
        MyThread th3 = instance.new MyThread(3); 
        MyThread th4 = instance.new MyThread(4);

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        try {
            th1.join(); 
            th2.join();
            th3.join();
            th4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		for (int i =0; i<4;i++){
			proba += instance.resultats[i];
		}
		System.out.println(proba);
    }
}