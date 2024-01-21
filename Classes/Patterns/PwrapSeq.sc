/* This one is taken from Mads Kjeldgaard's */
/* https://github.com/madskjeldgaard/Monolithic/blob/main/Classes/Pattern/PwrapSeq.sc */
PwrapSeq{
    *new{
        arg array, maxIndex, startIndex=0, repeats=inf;

        var indexPat = Pseries.new(startIndex, 1, length: inf) % (maxIndex+1);

        ^Pindex.new(listPat: array, indexPat: indexPat, repeats: repeats)
    }

}
