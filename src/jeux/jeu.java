package jeux;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import javax.swing.JButton;
public class jeu {
	public static class grille{
		public int n;
		public cel[][] tab;
	}
	public static cel corrigeCel(cel c) {
		if(c.val==1)
			c.val=0;
		else if (c.val==0)
			c.val=1;
		return(c);
	}
 	public static cel creerCel(int i,int j) {
		cel c=new cel();
	    Scanner in = new Scanner(System.in);
		c.val=in.nextInt();
		c.indi=i;
		c.indj=j;
		c.modifiable=1;
		return(c);
	}
	public static cel randomCel(int i,int j) {//remplir une cellule bien d�termin� avec 1 ou 0 d'une maniere al�atoire
		cel c=new cel();
		int nb =(int)(Math.random() * 2);
		c.val=nb;
		c.indi=i;
		c.indj=j;
		c.modifiable=0;
		return(c);
	}
	public static grille insertLigne(grille g,ligne li,int i) {
		for(int j=0;j<g.n;j++) {
			g.tab[i][j]=li.l[j];
		}
		return(g);
	}
	public static grille viderCellules(grille g) {
		grille f;
		f=initial(g.n);
		for(int i=0;i<g.n;i++) {
				for(int j=0;j<g.n;j++) {
					f.tab[i][j].val=g.tab[i][j].val;
					f.n=g.n;
					f.tab[i][j].indi=g.tab[i][j].indi=i;
					f.tab[i][j].indj=g.tab[i][j].indj=j;
					f.tab[i][j].modifiable=g.tab[i][j].modifiable=0;
				}
			}
		int [][] table=new int[g.n][g.n];
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				table[i][j]=0;
			}
		}
		do {
		int i=(int)(Math.random() * g.n);
		int j=(int)(Math.random() * g.n);
				int a=f.tab[i][j].val;
				table[i][j]=1;
				f.tab[i][j].val=-1;
				f.tab[i][j].modifiable=1;
				if(nbSol4(f)!=1 && f.n==4 || nbSol6(f)!=1 && (f.n==6 || f.n==8)) {
					f.tab[i][j].val=a;
				}
		}while(complet(table,g.n)==0);
		return(f);
		}
	public static grille initialGrille(init4 m,grille g) {
		g=viderGrille(g);
		int t =(int)(Math.random() * 2);
		System.out.println("t= "+t);
		if(t==0) {
			g=remplirPoss(m.poss1,g);
		}
		if(t==1) {
			g=remplirPoss(m.poss2,g);
		}
		return(g);
	}
	public static grille remplirPoss(int [][] poss, grille g) {
		int a=0;
		int []t= {0,0,0,0};
		do {
			int u =(int)(Math.random() * g.n);
			int v =(int)(Math.random() * g.n);
			if(t[v]==0) {
			if(nonVideLigne(u,g)==0) {
				t[v]=1;	
			for (int j=0;j<g.n;j++) {
				g.tab[u][j].val=poss[v][j];
			}	
		a++;
		}
			}
			}while(a<g.n);
	return(g);
	}
	public static grille viderGrille(grille g) {
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				g.tab[i][j].val=-1;
				g.tab[i][j].modifiable=0;
			}
		}
	return(g);
	}
	public static grille randomGrille(grille g) {//remplir une grille avec des 0 et des 1 d'une mani�re al�atoire(n'est pas util)
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				g.tab[i][j]=randomCel(i,j);
			}
		}
	return(g);
	}
	public static grille initial(int taille) {//initialiser la matrice avec des cellules aleatoires
		grille g=new grille();
		g.n=taille;
		cel[][] tableau=new cel[g.n][g.n];
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				tableau[i][j]=randomCel(i,j);
				}
		}
		g.tab=tableau;
		return(g);
	}
	public static grille modifGrille(grille g) {
		int i,j;
		cel c=new cel();
		System.out.print("\nchoisir les coordonn�e du case que vous voulez acc�der\n-ecrire i et j\n");
		Scanner sc=new Scanner(System.in);
		i=sc.nextInt();
		j=sc.nextInt();
		if(i<g.n+1 && i>0 && j<g.n+1 && j>0) {
		i--;
		j--;
		if(g.tab[i][j].modifiable==1) {
			System.out.print("\n-ecrire le contenue du cellule\n");
			c=creerCel(i,j);
		}
		else
			c.val=3;
		}
		else
			c.val=3;
		if (testCel(c)!=1) {
			do {
				afficheGrille(g);
				System.out.print("valeur invalid ou cellule n'est pas modifiable\nveuillez r�essayer (1 ou 2 ou -1 pour cellule vide)\n"
						+ "choisir les coordonn�e du case que vous voulez acc�der \n-ecrire i et j\n");
				i=sc.nextInt();
				j=sc.nextInt();
				if(i<g.n+1 && i>0 && j<g.n+1 && j>0) {
				i--;
				j--;
				if(g.tab[i][j].modifiable==1) {
					System.out.print("\n-ecrire le contenue du cellule\n");
					c=creerCel(i,j);
				}
				else
					c.val=3;
				}
				else 
					c.val=3;
			}while(testCel(c)!=1);
		}
		g.tab[i][j]=c;
		return(g);
	}
	public static grille initialGrille6(init6 m,grille g) {
		int [] t=new int[14];
		int u;
		for(int i=0;i<14;i++) {
			t[i]=0;
			}
		u =(int)(Math.random() * 14);
		g=remplirLigne(g,0,m.poss[u]);
		t[u]=1;
		if(u%2==0) {
			g=remplirLigne(g,1,m.poss[u+1]);
			t[u+1]=1;
		}
		else {
			g=remplirLigne(g,1,m.poss[u-1]);
			t[u-1]=1;
		}
		for(int i=1;i<g.n;i++) {
			if(i%2==0) {
		do
		u =(int)(Math.random() * 14);
		while(t[u]==1);
		if(t[u]==0 && verifCond(g,i,m.poss[u])==1) {
			t[u]=1;
			g=remplirLigne(g,i,m.poss[u]);
			if(u%2==0) {
				g=remplirLigne(g,i+1,m.poss[u+1]);
				t[u+1]=1;
			}
			else {
				g=remplirLigne(g,i+1,m.poss[u-1]);
				t[u-1]=1;
			}
		}
		else {
			i--;
		}
		}
		}
		return(g);
		
		/*
		 * int [] t=new int[14];
		int u;
		for(int i=0;i<14;i++) {
			t[i]=0;
			}
		u =(int)(Math.random() * 14);
		g=remplirLigne(g,0,m.poss[u]);
		t[u]=1;
		do {
		u =(int)(Math.random() * 14);
		}while(t[u]==1);
		g=remplirLigne(g,1,m.poss[u]);
		t[u]=1;
		for(int i=2;i<g.n;i++) {
		u =(int)(Math.random() * 14);
		if(t[u]==0 && verifCond(g,i,m.poss[u])==1) {
			t[u]=1;
			g=remplirLigne(g,i,m.poss[u]);
		}
		else
			i--;
		}
		return(g);
		*/
	}
	public static grille initialGrille8(init8 m,grille g) {
		int [] t=new int[34];
		int u;
		for(int i=0;i<34;i++) {
			t[i]=0;
			}
		u =(int)(Math.random() * 34);
		g=remplirLigne(g,0,m.poss[u]);
		t[u]=1;
		if(u%2==0) {
			g=remplirLigne(g,1,m.poss[u+1]);
			t[u+1]=1;
		}
		else {
			g=remplirLigne(g,1,m.poss[u-1]);
			t[u-1]=1;
		}
		for(int i=1;i<g.n;i++) {
			if(i%2==0) {
		do
		u =(int)(Math.random() * 34);
		while(t[u]==1);
		if(t[u]==0 && verifCond(g,i,m.poss[u])==1) {
			t[u]=1;
			g=remplirLigne(g,i,m.poss[u]);
			if(u%2==0) {
				g=remplirLigne(g,i+1,m.poss[u+1]);
				t[u+1]=1;
			}
			else {
				g=remplirLigne(g,i+1,m.poss[u-1]);
				t[u-1]=1;
			}
		}
		else {
			i--;
		}
		}
		}
		return(g);
	}
	public static grille remplirLigne(grille g,int i,int []l) {
		for(int j=0;j<g.n;j++) {
			g.tab[i][j].val=l[j];
		}
		return(g);
	}
	public static grille reset(grille g) {
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				if (g.tab[i][j].modifiable==1)
					g.tab[i][j].val=-1;
			}
		}
		return(g);
	}
	public static grille setMod(grille g) {
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				if(g.tab[i][j].val==-1)
					g.tab[i][j].modifiable=1;
				else
					g.tab[i][j].modifiable=0;
				}
		}
		return(g);
	}
	public static String afficheTemps(chrono c) {
		String s,heur,min,sec;
		long dure_heur=c.dure/3600000;
		long dure_min=c.dure/60000-(dure_heur*60);
		long dure_sec=c.dure/1000-(dure_min*60)-(dure_heur*3600);
		if(dure_heur<10) {
			heur="0"+dure_heur;
		}
		else
			heur=dure_heur+"";
		if(dure_min<10) {
			min="0"+dure_min;
		}else
			min=dure_min+"";
		if(dure_sec<10) {
			sec="0"+dure_sec;
		}else
			sec=dure_sec+"";
		s=heur+":"+min+":"+sec;
	
	return(s);
	}
	public static void afficheCel(cel c) {//affichage du contenue du cellule
		if(c.val==-1)
			System.out.print("X  ");
		else
		System.out.print(c.val+"  ");
	}
	public static void ecrireFichier(String msg, String n) {
		    ObjectOutputStream oos;
		    try {
		      oos = new ObjectOutputStream(
		              new BufferedOutputStream(
		                new FileOutputStream(
		                  new File(n),true)));
		       		      //Nous allons �crire chaque objet Game dans le fichier
		      oos.writeObject(new String(msg));
		      //Ne pas oublier de fermer le flux !
		      oos.close();
		     
		        	
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }     	
	}
	public static void readFichier(String n) {
		  // The name of the file to open.
        String fileName = n;

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
	public static List<score> getFichier(String n) {
		  // The name of the file to open.
        String fileName = n;
        String s=null;
        List<score> table = new ArrayList<score>();
        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	s=line;
            	score sc=creerScore(s);
            	table.add(sc);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        try {			//vider le fichier
        	// append = false
        	FileWriter fw =new FileWriter(fileName, false);
        	fw.write("");
        	fw.close();
        } catch (IOException e) {
        	e.printStackTrace();
        } 
        return(table);
	}
	public static score creerScore(String ch) {
		score scor=new score();
		String t=ch.substring(ch.length()-8);
		int s = Integer.parseInt(t.substring(t.length()-2));	
		int m = Integer.parseInt(t.substring(t.length()-5,t.length()-3));
		int h = Integer.parseInt(t.substring(0,2));
		scor.duree=h*3600+m*60+s;
		t=ch.substring(ch.indexOf(":")+1);
		scor.name=t.substring(0,t.indexOf(":"));
		return(scor);
	}
	public static void setFichier(List<score> table, String name) {
		String line=new String();
		for(int i=0;i<table.size();i++) {
			String s,heur,min,sec;
			long dure_heur=table.get(i).duree/3600;
			long dure_min=table.get(i).duree/60-(dure_heur*60);
			long dure_sec=table.get(i).duree-(dure_min*60)-(dure_heur*3600);
			if(dure_heur<10) {
				heur="0"+dure_heur;
			}
			else
				heur=dure_heur+"";
			if(dure_min<10) {
				min="0"+dure_min;
			}else
				min=dure_min+"";
			if(dure_sec<10) {
				sec="0"+dure_sec;
			}else
				sec=dure_sec+"";
			s=heur+":"+min+":"+sec;
			int k=i+1;
			String ch=" "+k+" : "+table.get(i).name+" : "+s+"\n";
			ecrireFichier(ch,name);
			
			
		}
	}
	public static List<score> rangerScore(List<score> table){
		for(int i=0;i<table.size()-1;i++) {
			for(int j=i+1;j<table.size();j++) {
				score sc1=table.get(i);
				score sc2=table.get(j);
				if(sc2.duree<sc1.duree) {
					score sc=sc1;
					sc1=sc2;
					sc2=sc;
				}
				table.set(i,sc1);
				table.set(j,sc2);
			}
		}
		return(table);
	}
	public static void afficheGrille(grille g) {//jolie ad=ffichage de oute la grille
		System.out.print("\n     ");
		for(int k=0;k<g.n;k++) {
			System.out.print(k+1+"  ");
		}
		System.out.print("\n");
		for (int i=0;i<g.n;i++) {
			int l=i+1;
			System.out.print("\n"+l+"    ");
			for(int j=0;j<g.n;j++) {
				afficheCel(g.tab[i][j]);
			}
		}
		System.out.print("\n");

	}
	public static int verifCond(grille g,int i,int []t) {
		for(int v=0;v<g.n;v++) {//calcule du nombre de 0 et nbre de 1 dans les case presedentes de la meme colonne
			int nb0=0;
			if(t[v]==0)
				nb0++;
			int nb1=t[v];
			for(int k=0;k<i;k++) {
				nb1+=g.tab[k][v].val;
				if(g.tab[k][v].val==0) {
					nb0++;
				}
			}
		if(t[v]==g.tab[i-1][v].val && t[v]==g.tab[i-2][v].val || nb0>g.n/2 || nb1>g.n/2)
			//System.out.println(nb1+"   "+nb0);
			return(0);
		}
		return(1);
	}
	public static int test(int a,int b) {
		if ((a==b)&&(a==0)||(a==b)&&(a==1))
				return(1);
		else
			return(0);
	}
	public static int indNonVide(int i,int l,int m,grille g) {
		for(int o=l;o<m;o++) {
			if(nonVideLigne(i,g)!=0) {
				int n=nonVideLigne(i,g)-1;
				return(n);
			}
		}
		return(-1);
	}
	public static int nonVideLigne(int i,grille g) {
		for(int j=0;j<g.n;j++) {
			if(g.tab[i][j].val!=-1)
				return(i+1);
		}
		return(0);
	}
	public static int testCel(cel c1,cel c2) {
		if (c1.val==c2.val)
			return(1);
		else return(0);
	}
	public static int testCel(cel c) {//util dans la methode modifGrille
		int t=0;
		if ((c.val==1)||(c.val==0)||(c.val==-1))
			t=1;
		return(t);
	}
	public static int getChoice1() {
    	int choix;
		Scanner sc = new Scanner(System.in);
		choix=sc.nextInt();
		if((choix!=1)&&(choix!=2)) {
    	do{	
			System.out.print("choix invalid , veuillez re�ssayez\n1- choisir la difficult�e\n2- Visualiser les meilleurs scores\\n");
    		choix=sc.nextInt();
	    }while((choix!=1)&&(choix!=2));
	}
    	return(choix);
	}
	public static int getChoice3() {
    	int choix;
		Scanner sc = new Scanner(System.in);
		choix=sc.nextInt();
		if((choix!=1)&&(choix!=2)) {
    	do{	
			System.out.print("choix invalid , veuillez re�ssayez\n1- Retourner au menu principale      2- Quitter\n");
    		choix=sc.nextInt();
	    }while((choix!=1)&&(choix!=2));
	}
    	return(choix);
	}
	public static int complet(int [][] t,int s) {
		for(int i=0;i<s;i++) {
			for(int j=0;j<s;j++) {
				if(t[i][j]==0)
					return(0);
			}
			}
		return(1);
	}
	public static int complTab(int [] t,int s) {
		for(int i=0;i<s;i++) {
				if(t[i]!=0)
					return(0);
			}
		return(1);
	}
	public static int nbSol6(grille g) {
		int t=0,p=0;
		grille f=initial(g.n);
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				f.tab[i][j].val=g.tab[i][j].val;
				f.n=g.n;
				f.tab[i][j].indi=g.tab[i][j].indi=i;
				f.tab[i][j].indj=g.tab[i][j].indj=j;
			}
		}
		//difficile
		/*
		for(int i=0;i<f.n;i++) {
			int n0=0;
			int n1=1;
			for(int j=0;j<f.n;j++) {
				if(f.tab[i][j].val==0)
					n0++;
				if(f.tab[i][j].val==1)
					n1++;
			}
			if(n0==f.n/2) {
			for(int j=0;j<f.n;j++) {
				if(f.tab[i][j].val!=0)
					f.tab[i][j].val=1;
			}
			}
			if(n1==f.n/2) {
				for(int j=0;j<f.n;j++) {
					if(f.tab[i][j].val!=1)
						f.tab[i][j].val=0;
				}
				}
		}
		for(int j=0;j<f.n;j++) {
			int n0=0;
			int n1=1;
			for(int i=0;i<f.n;i++) {
				if(f.tab[i][j].val==0)
					n0++;
				if(f.tab[i][j].val==1)
					n1++;
			}
			if(n0==f.n/2) {
			for(int i=0;i<f.n;i++) {
				if(f.tab[i][j].val!=0)
					f.tab[i][j].val=1;
			}
			}
			if(n1==f.n/2) {
				for(int i=0;i<f.n;i++) {
					if(f.tab[i][j].val!=1)
						f.tab[i][j].val=0;
				}
				}
		}*/
		//fin difficile
	
		do{
		// pour 6*6	
		for(int i=0;i<f.n;i++) {
			for(int j=0;j<f.n-2;j++) {
			if(test(f.tab[i][j].val,f.tab[i][j+2].val)==1) {
				f.tab[i][j+1].val=1-f.tab[i][j].val;
		}
		}
		}
		for(int i=0;i<f.n-2;i++) {
			for(int j=0;j<f.n;j++) {
			if(test(f.tab[i][j].val,f.tab[i+2][j].val)==1) {
				f.tab[i+1][j].val=1-f.tab[i][j].val;
		}
		}
		}
		for(int i=0;i<f.n-2;i++) {
			for(int j=0;j<f.n;j++) {
			if(test(f.tab[i][j].val,f.tab[i+1][j].val)==1) {
				if(i<f.n-2)
				f.tab[i+2][j].val=1-f.tab[i][j].val;
				if(i>0)
				f.tab[i-1][j].val=1-f.tab[i][j].val;

		}
		}
		}
		for(int i=0;i<f.n;i++) {
			for(int j=0;j<f.n-1;j++) {
			if(test(f.tab[i][j].val,f.tab[i][j+1].val)==1) {
				if(j<f.n-2)
				f.tab[i][j+2].val=1-f.tab[i][j].val;
				if(j>0)
				f.tab[i][j-1].val=1-f.tab[i][j].val;
		}
		}
		}
		
		//juska here
		t++;
		}while(t<15);
			for(int i=0;i<f.n;i++) {
				for(int j=0;j<f.n;j++) {
					if(f.tab[i][j].val==-1)
						return(0);
				}
			}
			return(1);
	}	
	public static int nbSol4(grille g) {
		int t=0;
		grille f=initial(g.n);
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				f.tab[i][j].val=g.tab[i][j].val;
				f.n=g.n;
				f.tab[i][j].indi=g.tab[i][j].indi=i;
				f.tab[i][j].indj=g.tab[i][j].indj=j;
			}
		}
		do{
		for(int i=0;i<f.n;i++) {
				if(test(f.tab[i][0].val,f.tab[i][2].val)==1) {
					f.tab[i][1].val=1-f.tab[i][0].val;
					f.tab[i][3].val=1-f.tab[i][0].val;
			}
				if(test(f.tab[i][0].val,f.tab[i][3].val)==1) {
					f.tab[i][1].val=1-f.tab[i][0].val;
					f.tab[i][2].val=1-f.tab[i][0].val;
			}
				if(test(f.tab[i][0].val,f.tab[i][1].val)==1) {
					f.tab[i][3].val=1-f.tab[i][0].val;
					f.tab[i][2].val=1-f.tab[i][0].val;
			}
				if(test(f.tab[i][1].val,f.tab[i][2].val)==1) {
					f.tab[i][0].val=1-f.tab[i][1].val;
					f.tab[i][3].val=1-f.tab[i][1].val;
			}
				if(test(f.tab[i][1].val,f.tab[i][3].val)==1) {
					f.tab[i][0].val=1-f.tab[i][1].val;
					f.tab[i][2].val=1-f.tab[i][1].val;
			}
				if(test(f.tab[i][2].val,f.tab[i][3].val)==1) {
					f.tab[i][1].val=1-f.tab[i][2].val;
					f.tab[i][0].val=1-f.tab[i][2].val;
			}
		}
		for(int i=0;i<f.n;i++) {
			if(test(f.tab[0][i].val,f.tab[2][i].val)==1) {
				f.tab[1][i].val=1-f.tab[0][i].val;
				f.tab[3][i].val=1-f.tab[0][i].val;
		}
			if(test(f.tab[0][i].val,f.tab[3][i].val)==1) {
				f.tab[1][i].val=1-f.tab[0][i].val;
				f.tab[2][i].val=1-f.tab[0][i].val;
		}
			if(test(f.tab[0][i].val,f.tab[1][i].val)==1) {
				f.tab[3][i].val=1-f.tab[0][i].val;
				f.tab[2][i].val=1-f.tab[0][i].val;
		}
			if(test(f.tab[1][i].val,f.tab[2][i].val)==1) {
				f.tab[0][i].val=1-f.tab[1][i].val;
				f.tab[3][i].val=1-f.tab[1][i].val;
		}
			if(test(f.tab[1][i].val,f.tab[3][i].val)==1) {
				f.tab[0][i].val=1-f.tab[1][i].val;
				f.tab[2][i].val=1-f.tab[1][i].val;
		}
			if(test(f.tab[2][i].val,f.tab[3][i].val)==1) {
				f.tab[1][i].val=1-f.tab[2][i].val;
				f.tab[0][i].val=1-f.tab[2][i].val;
		}
	}
	t++;
	}while(t<15);
		for(int i=0;i<f.n;i++) {
			for(int j=0;j<f.n;j++) {
				if(f.tab[i][j].val==-1)
					return(0);
			}
		}
		return(1);
	}
	public static int getChoice2() {
    	int choix;
		Scanner sc = new Scanner(System.in);
		choix=sc.nextInt();
		if((choix!=1)&&(choix!=2)&&(choix!=3)) {
    	do{	
			System.out.print("choix invalid , veuillez re�ssayez\n1- facile (4*4)   2- moyenne (8*8)   3- difficile (12*12)\n");
    		choix=sc.nextInt();
	    }while((choix!=1)&&(choix!=2)&&(choix!=3));
	}
    	return(choix);
	}
	public static int verifGrille(grille g ,grille f) {
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				if(f.tab[i][j].val!=g.tab[i][j].val)
					return(0);
			}
		}
		return(1);
	}
	public static int nonVideGrille(grille g) {
		for(int i=0;i<g.n;i++) {
			for(int j=0;j<g.n;j++) {
				if(g.tab[i][j].val==-1)
					return(0);
			}
		}
	return(1);
	}
	public static chrono debut(chrono c) {
		c.td=System.currentTimeMillis();
		c.tf=0;
		c.dure=0;
		return(c);
	}
	public static chrono fin(chrono c) {
		c.tf=System.currentTimeMillis();
		c.dure=c.tf-c.td;
		return(c);
	}

	public static void main(String[] args){
	    grille gr,f;
		String s;
		List<score> table;
		int ret=0,ch=0;
	    int choix1,choix2,ordre;
		init4 m4=new init4();
		init6 m6=new init6();
		init8 m8=new init8();
		chrono c=new chrono();

		System.out.print("JEU TAKUZU\nVeuillez taper votre nom\n");
		Scanner sc=new Scanner(System.in);
		String name=sc.nextLine();
	do {
		System.out.print("1- Choisir la difficultée\n2- Visualiser les meilleurs scores\n");
		choix1=getChoice1();
		if(choix1==1) {
			System.out.print("1- facile (4*4)   2- moyenne (6*6)   3- difficile (8*8)\n");
			choix2=getChoice2();
			if(choix2==1) {
				ordre=4;
				gr=initial(ordre);
				gr=initialGrille(m4,gr);
				f=viderCellules(gr);
				f=setMod(f);
				c=debut(c);
				afficheGrille(f);
				do {
					
					modifGrille(f);
					afficheGrille(f);
				}while(nonVideGrille(f)!=1);
				if(verifGrille(f,gr)==1) {
					c=fin(c);
					s=afficheTemps(c);
					ecrireFichier(0+" : "+name+" : "+s+"\n","scores1.txt");
					table=getFichier("scores1.txt");
					table=rangerScore(table);
					setFichier(table,"scores1.txt");
					table.clear();
					System.out.printf("<<<<<<<<<---------  BRAVOOOO !   YOU WIN  --------->>>>>>>>>\n");
					System.out.printf("<<<<<<<<<---------  le temps écoulé est  "+s+"  --------->>>>>>>>>\n");
					System.out.println("veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
				}
				else {
					System.out.println("SORRY    YOU LOOOOSE  voici la correction");
					afficheGrille(gr);
					System.out.println("veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
			}
			}
			else if(choix2==2){
				ordre=6;
				gr=initial(ordre);
				gr=viderGrille(gr);
				gr=initialGrille6(m6,gr);
				f=viderCellules(gr);
				f=setMod(f);
				c=debut(c);
				afficheGrille(f);
				do {
					modifGrille(f);
					afficheGrille(f);
				}while(nonVideGrille(f)!=1);
				if(verifGrille(f,gr)==1) {
					c=fin(c);
				s=afficheTemps(c);
				ecrireFichier(0+" : "+name+" : "+s+"\n","scores2.txt");
				table=getFichier("scores2.txt");
				table=rangerScore(table);
				setFichier(table,"scores2.txt");
				table.clear();
				System.out.printf("<<<<<<<<<---------  BRAVOOOO !   YOU WIN  --------->>>>>>>>>\n");
				System.out.printf("<<<<<<<<<---------  le temps écoulé est  "+s+"  --------->>>>>>>>>\n");
				System.out.println("veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
				}
				else {
					System.out.println("SORRY    YOU LOOOOSE  voici la correction");
					afficheGrille(gr);
					System.out.println("veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
			}
			}else if (choix2==3){
				ordre=8;
				gr=initial(ordre);
				gr=viderGrille(gr);
				gr=initialGrille8(m8,gr);
				f=viderCellules(gr);
				f=setMod(f);
				c=debut(c);
				afficheGrille(f);
				do {
					modifGrille(f);
					afficheGrille(f);
				}while(nonVideGrille(f)!=1);
				if(verifGrille(f,gr)==1) {
				c=fin(c);
				s=afficheTemps(c);
				ecrireFichier(0+" : "+name+" : "+s+"\n","scores3.txt");
				table=getFichier("scores3.txt");
				table=rangerScore(table);
				setFichier(table,"scores3.txt");
				table.clear();
				System.out.printf("<<<<<<<<<---------  BRAVOOOO !   YOU WIN  --------->>>>>>>>>\n");
				System.out.printf("<<<<<<<<<---------  le temps écoulé est  "+s+"  --------->>>>>>>>>\n");
				System.out.println("veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
				}
				else {
					System.out.println("SORRY    YOU LOOOOSE  voici la correction");
					afficheGrille(gr);
					System.out.println("Veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
			}
			}
		}	
		else if(choix1==2) {
			System.out.print("Choisir :\n1-Scores du niveau facile    2-Scores du niveau moyenne   3-Scores du niveau difficiles");
			ch=getChoice2();
			if(ch==1)
				readFichier("scores1.txt");
			else if (ch==2)
				readFichier("scores2.txt");
			else
				readFichier("scores3.txt");
			System.out.println("Veuillez retourner au menu principale\n1- Retourner au menu principale      2- Quitter");
		}
		ret=getChoice3();
	}while(ret==1);
	}
}