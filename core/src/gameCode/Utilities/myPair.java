package gameCode.Utilities;

import java.util.Objects;

public class myPair <A, B> {


    public A first;
    public B second;

    public myPair(A a, B b) {
        first = a;
        second = b;
    }



    /*
    not really sure exactly what how the first function works
    https://stackoverflow.com/questions/2265503/why-do-i-need-to-override-the-equals-and-hashcode-methods-in-java
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        myPair<?, ?> myPair = (myPair<?, ?>) o;
        return first.equals(myPair.first) &&
                second.equals(myPair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }








    /*
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((first == null) ? 0 : first.hashCode()) + ((second == null) ? 0 : second.hashCode());


        System.out.println(result);

        return result;
    }




    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final myPair other = (myPair) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        }
        if (second == null) {
            if (other.second != null)
                return false;
        }
        else if (!first.equals(other.first) || !second.equals(other.second))
            return false;
        return true;
    }

     */
}
