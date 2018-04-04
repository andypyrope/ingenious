# ingenious

Travis build status: [![Build Status](https://travis-ci.org/andypyrope/ingenious.svg?branch=master)](https://travis-ci.org/andypyrope/ingenious)

---

## Overview
#### The way in which a program combines a fitness calculator, a world and a UI
- `public static void main(String[] args) { ... }`
    - [CalculatorSettings - optional]: user-defined settings for the calculators in the world
    - **CalculatorProvider**: used by the World to provide a corresponding calculator
    - **World**
    - App (UI) / **Interactor**

The `main` method instantiates a *fitness calculator provider*, which may or may not require specific settings. Then it instantiates a world with that calculator provider. Finally it instantiates an interactor with that world.

The general idea behind this is that a **world** can function properly with any kind of fitness **calculator** and it can be viewed/manipulated through any kind of **interactor**. This is as modular as it can get. It might get even better if the feedforward neural network is made to function with any kind of neurons or activation function, but considering how RPROP is one of the most efficient simple algorithms, it may not be necessary.

## Example calculator provider
#### The calculator provider instantiates a specific kind of Calculator, which takes DNA and determines the fitness of an organism
- Simple**CalculatorProvider**
    - Simple**Calcualtor** - just counts the number of 1s in the DNA that has been given to it

and...
- Candle**CalculatorProvider**
    - Candle**Calculator**
        - FeedforwardNeuralNetwork
            - RpropFeedforwardNeuron (or BackpropFeedforwardNeuron)
                - LogisticActivationFunction
        - Candle**Dataset**


## Example world
#### The world creates/kills organisms, allows them to create calculators with the calculator provider and the organisms pass their DNA to their personal calculator, which determines their fitness
- Simple**World**
    - Simple**Organism**
        - Int**Dna** (or Byte**Dna**)


## Containment
#### When everything has been initialized, this is how the objects should contain each other
- `public static void main(String[] args) { ... }`
    - `Interactor`
        - `World` or `World[]`, depending on what the interactor can handle
            - `CalculatorProvider`
                - `Dataset` [optional] - a dataset contains parsed data necessary for fitness calculation
            - `Organism[]`
                - `Dna` for the calculator
                - `Dna` [optional] - for the organism itself if it needs that (e.g. to determine its gender)
                - `Calculator`
                    - `NeuralNetwork` [optional] - basically the brain of the organism
