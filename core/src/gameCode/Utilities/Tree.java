package gameCode.Utilities;

import java.util.ArrayList;
import java.util.Collections;

public class Tree<Type> {

    public Tree parent;
    public ArrayList<Tree> children;
    public Type value;

    public Tree(Type newValue, Tree newParent) {
        children = new ArrayList<Tree>();
        parent = newParent;
        value = newValue;
        if(parent != null) parent.addChild(this);
    }

    public Type getValue() {
        Type retVal = (Type)value;
        return retVal;
    }

    public void setParent(Tree newParent) {
        if(parent != null) parent.children.remove(this);
        parent = newParent;
        if(parent != null) parent.addChild(this);
    }

    public void addChild(Tree newChild) { children.add(newChild); }

    public ArrayList<Tree> getTraverseArr() {

        class treeNode {
            public Tree node;
            public boolean checked;
            public treeNode(Tree newNode, boolean newCheck) {
                node = newNode;
                checked = newCheck;
            }
        }

        ArrayList<Tree> retVal = new ArrayList<Tree>();
        ArrayList<treeNode> order = new ArrayList<treeNode>();
        order.add(new treeNode(this, false));

        while(order.size() > 0) {

            //set the current last items check value to true
            order.get(order.size() - 1).checked = true;

            //add the children
            treeNode lastItem = order.get(order.size() - 1);
            for(int x = 0; x < lastItem.node.children.size(); x++) { order.add( new treeNode((Tree)lastItem.node.children.get(x), false)); }

            //check the last item in order. if it has no children or if check == true add it to retVal and move pop it off order
            while(order.size() > 0) {
                treeNode last = order.get(order.size() - 1);
                if(last.node.children.size() == 0 || last.checked) {
                    retVal.add(last.node);
                    order.remove(order.size() - 1);
                }
                else break;
            }
        }

        Collections.reverse(retVal);
        return retVal;
    }

    public ArrayList<Tree> getChildren() {

        class treeNode {
            public Tree node;
            public boolean checked;
            public treeNode(Tree newNode, boolean newCheck) {
                node = newNode;
                checked = newCheck;
            }
        }

        ArrayList<Tree> retVal = new ArrayList<Tree>();
        ArrayList<treeNode> order = new ArrayList<treeNode>();
        order.add(new treeNode(this, false));

        while(order.size() > 0) {

            //set the current last items check value to true
            order.get(order.size() - 1).checked = true;

            //add the children
            treeNode lastItem = order.get(order.size() - 1);
            for(int x = 0; x < lastItem.node.children.size(); x++) { order.add( new treeNode((Tree)lastItem.node.children.get(x), false)); }

            //check the last item in order. if it has no children or if check == true add it to retVal and move pop it off order
            while(order.size() > 0) {
                treeNode last = order.get(order.size() - 1);
                if(last.node.children.size() == 0 || last.checked) {
                    retVal.add(last.node);
                    order.remove(order.size() - 1);
                }
                else break;
            }
        }

        retVal.remove(retVal.size() - 1);
        Collections.reverse(retVal);
        return retVal;
    }
}
