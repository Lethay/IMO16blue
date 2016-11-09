package AgentGridMin;

import java.util.Random;

/**
 * Created by bravorr on 10/31/16.
 */
public class NeuralNet {
    public double[] neurons;
    public double[] biases;
    public double[] nextTickValues;
    public int[] connections;
    public double[] weights;
    public int[] iLayers;//index of first neuron in this layer, next number is 1+last in this layer
    public int[] iConns;//index of first connection for this neuron, next number is 1+last for this neuron, index maps to neuron index
    public NeuralNet(int maxNeurons,int maxConns,int nLayers){
        neurons=new double[maxNeurons];
        nextTickValues=new double[maxNeurons];
        biases=new double[maxNeurons];
        connections=new int[maxConns];
        weights=new double[maxConns];
        iConns=new int[maxNeurons+1];
        iLayers=new int[nLayers+1];
        Reset();
    }
    public void Copy(NeuralNet from){
       for(int i=0;i<connections.length;i++){
           connections[i]=from.connections[i];
       }
       for(int i=0;i<weights.length;i++){
           weights[i]=from.weights[i];
       }
       for(int i=0;i<iConns.length;i++){
           iConns[i]=from.iConns[i];
       }
       for(int i=0;i<iLayers.length;i++){
           iLayers[i]=from.iLayers[i];
       }
    }
    public void CopyWeightsAndBiases(NeuralNet from){
        for(int i=0;i<weights.length;i++){
            weights[i]=from.weights[i];
        }
        for(int i=0;i<biases.length;i++){
            biases[i]=from.biases[i];
        }
    }
    public void ClearNeurons(){
        for(int i=0;i<neurons.length;i++){
            neurons[i]=0;
        }
    }
    public void WeightsFromStringArray(String[] data,int iStart){
        for(int i=0;i<weights.length;i++){
            weights[i]=Double.parseDouble(data[i+iStart]);
        }
    }
    public void BiasesFromStringArray(String[] data,int iStart){
        for(int i=0;i<biases.length;i++){
            biases[i]=Double.parseDouble(data[i+iStart]);
        }
    }
    public void Reset(){
        for(int i=0;i<iConns.length;i++){
            iConns[i]=0;
        }
        for(int i=0;i<iLayers.length;i++){
            iLayers[i]=0;
        }
        for(int i=0;i<nextTickValues.length;i++){
            nextTickValues[i]=0;
        }
    }
    void ShiftNeurons(int iStart,int iLayer,int delta){
        int nNeurons=iLayers[iLayers.length-1];
        if(delta>0) {
            for (int i = nNeurons-1; i >= iStart; i--) {
                neurons[i + delta] = neurons[i];
                iConns[i+delta+1]=iConns[i+1];
                biases[i+delta]=biases[i];
                nextTickValues[i+delta]=nextTickValues[i];
            }
        }
        else if(delta<0){
            for(int i = iStart;i<nNeurons;i++){
                neurons[i+delta]=neurons[i];
                biases[i+delta]=biases[i];
                nextTickValues[i+delta]=nextTickValues[i];
                iConns[i+delta+1]=iConns[i+1];
            }
        }
        if(delta!=0) {
            for (int i = iLayer + 1; i < iLayers.length; i++) {
                iLayers[i] += delta;
            }
        }
    }
    void ShiftConnections(int iStart,int iNeuron,int delta){
        int nNeurons=iLayers[iLayers.length-1];
        int nConnections=iConns[nNeurons];
        if(delta>0){
            for(int i=nConnections-1;i>=iStart;i--){
                connections[i+delta]=connections[i];
                weights[i+delta]=weights[i];
            }
        }
        else if(delta<0){
            for(int i=iStart;i<nConnections;i++){
                connections[i+delta]=connections[i];
                weights[i+delta]=weights[i];
            }
        }
        if(delta!=0) {
            for (int i = iNeuron + 1; i <= nNeurons; i++) {
                iConns[i] += delta;
            }
        }
    }
    public int INeuron(int layer,int index){
        return iLayers[layer]+index;
    }
    public boolean NeuronExists(int layer,int index){
        return iLayers[layer+1]>index;
    }
    int INeuronInLayer(int layer,int iNeuron){
        return iNeuron-iLayers[layer];
    }
    int IConnection(int iNeuron,int iNeuronTo){
        for(int i=iConns[iNeuron];i<iConns[iNeuron+1];i++){
            if(connections[i]==iNeuronTo){
                return i;
            }
        }
        //not found
        return -1;
    }
    //    public boolean ConnectionExists(int layer,int iFrom,int iTo){

    public void AddNeuron(int layer,double initVal,double bias){
        ShiftNeurons(iLayers[layer],layer,1);
        int iNewNeuron=iLayers[layer+1]-1;
        neurons[iNewNeuron]=initVal;
        biases[iNewNeuron]=bias;
    }
    public void RemoveNeuron(int layer,int index){
        if(!NeuronExists(layer,index)){
            throw new IllegalArgumentException("neuron never existed!");
        }
        int iNeuron=INeuron(layer,index);
        ShiftConnections(iConns[iNeuron+1],iNeuron,iConns[iNeuron]-iConns[iNeuron+1]);
        ShiftNeurons(iNeuron+1,layer,-1);
    }
    public void RemoveConnection(int layerFrom,int iFrom,int layerTo,int iTo){
        int iNeuronFrom=INeuron(layerFrom,iFrom);
        int iNeuronTo=INeuron(layerTo,iTo);
        int iConn=IConnection(iNeuronFrom,iNeuronTo);
        if(iConn!=-1){
            ShiftConnections(iConn+1,iNeuronFrom,-1);
        }
        else{
            throw new IllegalArgumentException("connection never existed!");
        }
    }
    public void AddConnection(int layerFrom,int iFrom,int layerTo,int iTo,double weight){
        if(!NeuronExists(layerFrom,iFrom)||!NeuronExists(layerTo,iTo)){
            throw new IllegalArgumentException("one of the neurons does not exist!");
        }
        int iNeuronFrom=INeuron(layerFrom,iFrom);
        int iNeuronTo=INeuron(layerTo,iTo);
        int iAdd=iConns[iNeuronFrom+1];
        for(int i=iConns[iNeuronFrom];i<=iConns[iNeuronFrom+1];i++){
            if(connections[i]==iNeuronTo){
                throw new IllegalArgumentException("connection already exists!");
            }
            if(connections[i]>iNeuronTo){
                iAdd=i;
                break;
            }
        }
        ShiftConnections(iAdd,iNeuronFrom,1);
        connections[iAdd]=iNeuronTo;
        weights[iAdd]=weight;
    }
    public void RunNetwork(double scale,boolean skipFirstLayer){
        int iNstart=iLayers[1];
        int iNend=iLayers[iLayers.length-1];
        for(int iNeuron=iNstart;iNeuron<iNend;iNeuron++){
            neurons[iNeuron]=0;
        }
            //zero all neuron values after the first layer
       for(int iLayer=0;iLayer<iLayers.length-1;iLayer++) {
           iNstart=iLayers[iLayer];
           iNend=iLayers[iLayer+1];
           if(!skipFirstLayer||iLayer>0) {
               for (int iNeuron = iNstart; iNeuron < iNend; iNeuron++) {
                   neurons[iNeuron]+=nextTickValues[iNeuron]+biases[iNeuron];
                   nextTickValues[iNeuron]=0;
                   neurons[iNeuron] = Math.tanh(neurons[iNeuron] * scale);
               }
           }
           //apply nextTickValues, biases, and tanh function. may skip for first layer
           for(int iNeuron=iNstart;iNeuron<iNend;iNeuron++){
               int iConnStart=iConns[iNeuron];
               int iConnEnd=iConns[iNeuron+1];
               for(int iConn=iConnStart;iConn<iConnEnd;iConn++){
                   neurons[connections[iConn]]+=neurons[iNeuron]*weights[iConn];
               }
           }
       }
    }
    public int IConnections(int layer,int index){
        return iConns[INeuron(layer,index)];
    }
    public int IConnections(int iNeuron){
        return iConns[iNeuron];
    }
    public int NConnections(int layer,int index){
        int iNeuron=INeuron(layer,index);
        return iConns[iNeuron+1]-iConns[iNeuron];
    }
    public int NConnections(int iNeuron){
        return iConns[iNeuron+1]-iConns[iNeuron];
    }
    public int CountConnections(){
        return iConns[iLayers[iLayers.length-1]];
    }
    public int CountNeurons(){
        return iLayers[iLayers.length-1];
    }
    public void PrintConns(){
        for(int iLayer=0;iLayer<iLayers.length-2;iLayer++){//layers that can have connections!
            for(int iNeuron=iLayers[iLayer];iNeuron<iLayers[iLayer+1];iNeuron++){
                for(int iConn=iConns[iNeuron];iConn<iConns[iNeuron+1];iConn++){
                    System.out.println("Layer: "+iLayer+" From: "+INeuronInLayer(iLayer,iNeuron)+" To: "+INeuronInLayer(iLayer+1,connections[iConn])+" Weight: "+weights[iConn]);
                }
            }
        }
    }
    public void PrintNet(){
        for(int i=0;i<iLayers.length-1;i++){
            for(int j=iLayers[i];j<iLayers[i+1];j++){
                System.out.print(neurons[j]+",");
            }
            System.out.println("");
        }
    }
    public void MutateConns(double stdDev,Random rn){
        int nConns=iConns[iLayers[iLayers.length-1]];
        for(int i=0;i<nConns;i++){
            weights[i]=Utils.BoundVal(weights[i]+Utils.Gaussian(0,stdDev,rn),-1,1);
        }
    }
    public void MutateBiases(double stdDev,boolean skipFirstLayer,Random rn){
        int nBiases=iLayers[iLayers.length-1];
        int i=0;
        if(skipFirstLayer){
            i=iLayers[1];
        }
        for(;i<nBiases;i++){
            biases[i]=Utils.BoundVal(biases[i]+Utils.Gaussian(0,stdDev,rn),-1,1);
        }
    }
}
