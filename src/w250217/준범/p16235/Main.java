package w250217.준범.p16235;

import java.util.*;
import java.io.*;

class Main {
    static class Tree implements Comparable<Tree> {
        int x;
        int y;
        int age;

        Tree(int x, int y, int age) {
            this.x = x;
            this.y = y;
            this.age = age;
        }

        @Override
        public int compareTo(Tree t) {
            return this.age - t.age;
        }
    }

    static int n;
    static int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
    static int[] dy = { 1, -1, 0, 0, -1, 1, 1, -1 };

    static int[][] ground; // 땅의 양분 정보
    static int[][] A; // 더해질 A

    // 나무 저장
    // 필요한 정보 : 좌표, age -> 나무들을 좌표 순서대로 접근 할 필요가 없음 : 3차원 배열 사용X
    // 나이 순서대로 접근해야함 -> 나이 기준 정렬 위해 Comparable, PQ 사용
    static PriorityQueue<Tree> trees = new PriorityQueue<>();

    // 죽은 나무 임시 저장용 : spring, summer (구분된 함수) 간 연결 위해 static 사용함
    static List<Tree> death = new ArrayList<>();

    static void spring() {
        PriorityQueue<Tree> temp = new PriorityQueue<>();
        while (!trees.isEmpty()) {
            Tree t = trees.poll();
            int x = t.x;
            int y = t.y;
            int age = t.age;
            if (ground[x][y] < age) {
                death.add(t);
            } else {
                ground[x][y] -= age;
                t.age++;
                temp.add(t);
            }
        }
        // 참조 변경 오류 발생 가능. trees를 여러 함수에서 사용할 경우 참조를 유지하는 방법 사용
        // trees = temp;
        trees.clear();
        trees.addAll(temp);

    }

    static void summer() {
        for (Tree t : death) {
            ground[t.x][t.y] += (t.age / 2);
        }
        death.clear();
    }

    static void fall() {
        PriorityQueue<Tree> temp = new PriorityQueue<>();
        for (Tree t : trees) {
            if (t.age % 5 == 0) {
                for (int i = 0; i < 8; i++) {
                    int nx = t.x + dx[i];
                    int ny = t.y + dy[i];
                    int age = 1;
                    if (nx < 0 || ny < 0 || n <= nx || n <= ny)
                        continue;
                    temp.add(new Tree(nx, ny, age));

                }
            }
        }
        // Iterator 사용하는 도중에 collection를 변경하면 ConcurrentModificationException 발생 가능
        trees.addAll(temp);
    }

    static void winter() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ground[i][j] += A[i][j];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());

        ground = new int[n][n];
        A = new int[n][n];
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        for (int i = 0; i < n; i++) {
            Arrays.fill(ground[i], 5);
        }

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                A[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int age = Integer.parseInt(st.nextToken());
            trees.add(new Tree(x, y, age));
        }

        for (int i = 0; i < k; i++) {
            spring();
            summer();
            fall();
            winter();
        }

        System.out.println(trees.size());

    }
}