# ingenious

Travis build status: [![Build Status](https://travis-ci.org/andypyrope/ingenious.svg?branch=master)](https://travis-ci.org/andypyrope/ingenious)

---

## Overview
#### The way in which a program combines a fitness calculator, a world and a UI
- `public static void main(String[] args) { ... }`
    - [CalculatorSettings - optional]: user-defined settings for the calculators in the world
    - **CalculatorProvider**: used by the World to provide a corresponding calculator
    - **World**
    - App (UI)

The `main` method instantiates a *fitness calculator provider*, which may or may not require specific settings.
Then it instantiates a world with that calculator provider. Finally it instantiates a UI with that world.

The general idea behind this is that a **world** can function properly with any kind of fitness **calculator** and it
can be viewed/manipulated through any kind of **UI**. This is as modular as it can get. It might get even better
if the feedforward neural network is made to function with any kind of neurons or activation function, but considering
how RPROP is one of the most efficient simple algorithms, it may not be necessary.

## Example calculator provider
#### The calculator provider instantiates a specific kind of Calculator, which takes DNA and determines the fitness of
an organism
- Simple**CalculatorProvider**
    - Simple**Calculator** - just counts the number of 1s in the DNA that has been given to it

and...
- Candle**CalculatorProvider**
    - Candle**Calculator**
        - FeedforwardNeuralNetwork
            - RpropFeedforwardNeuron (or BackpropFeedforwardNeuron)
                - LogisticActivationFunction
        - Candle**Dataset**


## Example world
#### The world creates/kills organisms, allows them to create calculators with the calculator provider and the organisms
pass their DNA to their personal calculator, which determines their fitness
- Simple**World**
    - Simple**Organism**
        - Int**Dna** (or Byte**Dna**)


## Containment
#### When everything has been initialized, this is how the objects should contain each other
- `public static void main(String[] args) { ... }`
    - `App`
        - `World` or `World[]`, depending on what the app can handle
            - `CalculatorProvider`
                - `Dataset` [optional] - a dataset contains parsed data necessary for fitness calculation
            - `Organism[]`
                - `Dna` for the calculator
                - `Dna` [optional] - for the organism itself if it needs that (e.g. to determine its gender)
                - `Calculator`
                    - `NeuralNetwork` [optional] - basically the brain of the organism

## AI
#### The brains of the organisms. The neural networks can also be used outside of the context of organisms.
Currently there is only one type of neural networks in this project - atomic. The input of these networks is always of type `double[]` (i.e. numeric - one real value for each input neuron) and so is their output. The way they calculate the output and learn can vary greatly depending on what kind of a network it is and how it has been initialized. For now only a feedforward network class has been made, but there can be potentially even circular networks, for example a Hopfield network, or ones with an irregular shape.

Atomic network: :hash::arrow_right::hash:

Another 3 basic kinds of networks are planned:
- :camera::arrow_right::hash: Convolutional network (image input -> numeric output)
- :hash::arrow_right::camera: Deconvolutional network (numeric input -> image output)
- :camera::arrow_right::camera: Convolutional autoencoder (image input -> image output)

Image data will most probably be a `double[][][]` array - two dimensions for width and height, and one of depth in case the image has colours. The same kind of data can be used for audio (X is time, Y is pitch, and there is no depth) or even one-dimensional functions (X is, well, X, and there is no width or height).

The filters used in the convolutional networks will be of image type as well and should have width, height and depth lower than or equal to that of the image data that goes through them.
